package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.altar.PowerConsumer;
import net.favouriteless.enchanted.api.altar.PowerProvider;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.enchanted.altar.AltarBlockData;
import net.favouriteless.enchanted.common.enchanted.altar.AltarUpgradeData;
import net.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import net.favouriteless.enchanted.common.menus.AltarMenu;
import net.favouriteless.enchanted.common.enchanted.stateobservers.AltarStateObserver;
import net.favouriteless.stateobserver.api.StateObserverManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

// TODO: Rewrite. Bad code.
public class AltarBlockEntity extends BlockEntity implements MenuProvider, PowerProvider {

    private final double rechargeRate = ServerConfig.INSTANCE.altarBaseRecharge.get();
    private final AltarBlockData altarBlockData = new AltarBlockData();
    private final AltarUpgradeData altarUpgradeData = new AltarUpgradeData();
    private double rechargeMultiplier = 1.0D;
    private double powerMultiplier = 1.0D;
    private double maxPower;
    private double currentPower;

    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int index) {
            return switch(index) {
                case 0 -> (int) Math.round(currentPower);
                case 1 -> (int) Math.round(maxPower);
                case 2 -> (int) Math.round(rechargeMultiplier);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    currentPower = value;
                case 1:
                    maxPower = value;
                case 2:
                    rechargeMultiplier = value;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private AltarStateObserver stateObserver = null;
    private boolean facingX;
    private Vec3 centerPos;

    private boolean firstLoad = true;
    private int ticksAlive = 0;
    private boolean firstTick = true;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.ALTAR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AltarBlockEntity be) {
        if(be.firstTick) // Can't use onLoad here because it's forge exclusive.
            be.firstTick();

        if(be.ticksAlive % 20 == 0) {
            be.stateObserver.checkChanges();
        }
        if(be.currentPower <= be.maxPower)
            be.currentPower += be.rechargeRate * be.rechargeMultiplier;
        if(be.currentPower > be.maxPower)
            be.currentPower = be.maxPower;
        be.ticksAlive++;
    }

    public void firstTick() {
        if(stateObserver == null)
            stateObserver = StateObserverManager.get().getObserver(level, worldPosition, AltarStateObserver.class);
        if(stateObserver == null) {
            int range = ServerConfig.INSTANCE.altarRange.get();
            stateObserver = StateObserverManager.get().addObserver(new AltarStateObserver(level, worldPosition, range + 4, range + 4, range + 4));
        }
        facingX = level.getBlockState(worldPosition).getValue(AltarBlock.FACING_X);
        centerPos = facingX ?
                Vec3.atLowerCornerOf(worldPosition).add(1.0D, 0.0D, 0.5D) :
                Vec3.atLowerCornerOf(worldPosition).add(0.5D, 0.0D, 1.0D);
        if(firstLoad)
            recalculateAll();
        firstTick = false;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, Provider provider) {
        nbt.putDouble("currentPower", currentPower);
        nbt.putDouble("maxPower", maxPower);
        nbt.putDouble("powerMultiplier", powerMultiplier);
        nbt.putDouble("rechargeMultiplier", rechargeMultiplier);
        nbt.put("blockData", altarBlockData.save());
        nbt.put("upgradeData", altarUpgradeData.save(level));
    }

    @Override
    public void loadAdditional(CompoundTag nbt, Provider provider) {
        setMaxPower(nbt.getDouble("maxPower"));
        currentPower = nbt.getDouble("currentPower");
        powerMultiplier = nbt.getDouble("powerMultiplier");
        rechargeMultiplier = nbt.getDouble("rechargeMultiplier");
        if(nbt.contains("blockData") && nbt.contains("upgradeData")) {
            altarBlockData.load(nbt.getCompound("blockData"));
            altarUpgradeData.load(nbt.getCompound("upgradeData"));
        }
        else
            Enchanted.LOG.error("Failed to load power block data for altar at {}", getBlockPos().toShortString());

        firstLoad = false;
    }

    /**
     * Clear the {@link AltarBlockData} and {@link AltarUpgradeData} for this {@link AltarBlockEntity} and recalculate
     * their values.
     */
    private void recalculateAll() {
        recalculateUpgrades();
        recalculateBlocks();
    }

    /**
     * Clear the {@link AltarUpgradeData} for this {@link AltarBlockEntity} and recalculate its values.
     */
    private void recalculateUpgrades() {
        if(level != null && !level.isClientSide) {
            BlockPos minPos = worldPosition.above();
            BlockPos maxPos = facingX ? minPos.offset(2, 0, 1) : minPos.offset(1, 0, 2);

            altarUpgradeData.reset();
            for(BlockPos pos : BlockPos.betweenClosed(minPos, maxPos))
                altarUpgradeData.addBlock(level, level.getBlockState(pos).getBlock());

            powerMultiplier = altarUpgradeData.calculatePowerMultiplier(level);
            rechargeMultiplier = altarUpgradeData.calculateRechargeMultiplier(level);
            setChanged();
        }
    }

    /**
     * Clear the {@link AltarBlockData} for this {@link AltarBlockEntity} and recalculate its values.
     */
    private void recalculateBlocks() {
        if(level != null && !level.isClientSide) {
            int range = ServerConfig.INSTANCE.altarRange.get();
            BlockPos startingPos = facingX ?
                    BlockPos.containing(centerPos.add(-(range+4), -(range+2), -(range+2))) :
                    BlockPos.containing(centerPos.add(-(range+2), -(range+2), -(range+4)));

            altarBlockData.reset();
            for (int x = 0; x < (range+2)*2; x++) {
                for (int y = 0; y < (range+2)*2; y++) {
                    for (int z = 0; z < (range+2)*2; z++) {
                        BlockPos currentPos = startingPos.offset(x, y, z);

                        if(posWithinRange(currentPos, range)) {
                            if(level.getBlockEntity(currentPos) instanceof PowerConsumer consumer)
                                consumer.getPosHolder().add(worldPosition); // Notify consumers that this altar exists.

                            addBlock(level.getBlockState(currentPos).getBlock());
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if a given position is within this Altar's range.
     *
     * @param pos The {@link BlockPos} to check.
     * @param range The range to check for.
     *
     * @return True if in range, otherwise false.
     */
    public boolean posWithinRange(BlockPos pos, int range) {
        if(this.level != null) {
            double rx = facingX ? range+1 : range;
            double rz = facingX ? range : range+1;
            double dx = pos.getX() - centerPos.x;
            double dy = pos.getY() - centerPos.y;
            double dz = pos.getZ() - centerPos.z;
            return (dx * dx) / (rx * rx) + (dy * dy) / (range * range) + (dz * dz) / (rz * rz) <= 1;
        }
        return false;
    }

    /**
     * Check if a given position is an upgrade (i.e. on top of the Altar).
     *
     * @param pos The {@link BlockPos} to check.
     *
     * @return True if position is an upgrade, otherwise false.
     */
    public boolean posIsUpgrade(BlockPos pos) {
        if(this.level != null) {
            int xMax;
            int zMax;

            if(facingX) {
                xMax = 2;
                zMax = 1;
            } else {
                xMax = 1;
                zMax = 2;
            }

            int xOffset = pos.getX() - worldPosition.getX();
            int zOffset = pos.getZ() - worldPosition.getZ();
            boolean withinX = xOffset >= 0 && xOffset <= xMax;
            boolean withinZ = zOffset >= 0 && zOffset <= zMax;
            return pos.getY() == worldPosition.getY()+1 && withinX && withinZ;
        }
        return false;
    }

    /**
     * Set the maximum power for this Altar and clamp the current power to it.
     *
     * @param power New max power.
     */
    public void setMaxPower(double power) {
        maxPower = power;
        if(currentPower > maxPower)
            currentPower = maxPower;
        setChanged();
    }

    /**
     * Add a block to this {@link AltarBlockEntity}'s {@link AltarBlockData} and calculate the power gain from it.
     *
     * @param block The {@link Block} to add.
     */
    public void addBlock(Block block) {
        setMaxPower(maxPower + altarBlockData.addBlock(level, block) * powerMultiplier);
        setChanged();
    }

    /**
     * Remove a block from this {@link AltarBlockEntity}'s {@link AltarBlockData} and calculate the power loss from it.
     *
     * @param block The {@link Block} to remove.
     */
    public void removeBlock(Block block) {
        setMaxPower(maxPower - altarBlockData.removeBlock(level, block) * powerMultiplier);
        setChanged();
    }

    /**
     * Add a block to this {@link AltarBlockEntity}'s {@link AltarUpgradeData} and calculate the power gain from it.
     *
     * @param block The {@link Block} to add.
     */
    public void addUpgrade(Block block) {
        if(altarUpgradeData.addBlock(level, block)) {
            double newPowerMultiplier = altarUpgradeData.calculatePowerMultiplier(level);
            if(newPowerMultiplier != powerMultiplier) {
                powerMultiplier = newPowerMultiplier;
                setMaxPower(altarBlockData.calculatePower(level, powerMultiplier));
            }
            rechargeMultiplier = altarUpgradeData.calculateRechargeMultiplier(level);
            setChanged();
        }
    }

    /**
     * Remove a block from this {@link AltarBlockEntity}'s {@link AltarUpgradeData} and calculate the power loss from it.
     *
     * @param block The {@link Block} to remove.
     */
    public void removeUpgrade(Block block) {
        if(altarUpgradeData.removeBlock(level, block)) {
            double newPowerMultiplier = altarUpgradeData.calculatePowerMultiplier(level);
            if(newPowerMultiplier != powerMultiplier) {
                powerMultiplier = newPowerMultiplier;
                setMaxPower(altarBlockData.calculatePower(level, powerMultiplier));
            }
            rechargeMultiplier = altarUpgradeData.calculateRechargeMultiplier(level);
            setChanged();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.enchanted.altar");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AltarMenu(id, this, fields);
    }

    @Override
    public boolean tryConsume(double amount) {
        if(this.currentPower > amount) {
            currentPower -= amount;
            return true;
        }
        return false;
    }

}
