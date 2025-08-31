package net.favouriteless.enchanted.common.blocks.entity;

import net.favouriteless.enchanted.api.altar.PowerConsumer;
import net.favouriteless.enchanted.api.altar.PowerProvider;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import net.favouriteless.enchanted.common.enchanted.altar.AltarPowerData;
import net.favouriteless.enchanted.common.menus.AltarMenu;
import net.favouriteless.enchanted.common.enchanted.stateobservers.AltarStateObserver;
import net.favouriteless.enchanted.common.util.NonSettableContainerData;
import net.favouriteless.stateobserver.api.StateObserverManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AltarBlockEntity extends BlockEntity implements MenuProvider, PowerProvider {

    private final AltarPowerData powerData = new AltarPowerData();

    private double power;

    private AltarStateObserver stateObserver = null;
    private boolean facingX;
    private Vec3 centerPos;

    private boolean firstLoad = true;
    private boolean firstTick = true;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.ALTAR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AltarBlockEntity be) {
        if(be.firstTick)
            be.firstTick();
        if(level.getGameTime() % 20 == 0)
            be.stateObserver.checkChanges();

        double capacity = be.powerData.getCapacity();

        if(be.power < capacity)
            be.power += ServerConfig.INSTANCE.altarBaseRecharge.get() * be.powerData.getRechargeMultiplier();
        if(be.power > capacity)
            be.power = capacity;
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
            setupPowerData();

        powerData.validate(level);
        firstTick = false;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, Provider registries) {
        nbt.putDouble("power", power);
        nbt.put("powerData", powerData.save(level));
    }

    @Override
    public void loadAdditional(CompoundTag nbt, Provider registries) {
        power = nbt.getDouble("power");
        if(nbt.contains("powerData"))
            powerData.load(nbt.getCompound("powerData"), registries);
        else
            Enchanted.LOG.error("Failed to load power data for altar at {}", getBlockPos().toShortString());
        firstLoad = false;
    }

    private void setupPowerData() {
        powerData.reset();
        setupBlockData();
        setupUpgradeData();
        setChanged();
    }

    private void setupUpgradeData() {
        if(level == null || level.isClientSide)
            return;

        BlockPos minPos = worldPosition.above();
        BlockPos maxPos = facingX ? minPos.offset(2, 0, 1) : minPos.offset(1, 0, 2);

        for(BlockPos pos : BlockPos.betweenClosed(minPos, maxPos))
            powerData.addUpgrade(level, level.getBlockState(pos).getBlock());
    }

    private void setupBlockData() {
        if(level == null || level.isClientSide)
            return;

        int range = ServerConfig.INSTANCE.altarRange.get();
        BlockPos startingPos = facingX ?
                BlockPos.containing(centerPos.add(-(range+4), -(range+2), -(range+2))) :
                BlockPos.containing(centerPos.add(-(range+2), -(range+2), -(range+4)));

        for(int x = 0; x < (range+2) * 2; x++) {
            for(int y = 0; y < (range+2) * 2; y++) {
                for(int z = 0; z < (range+2) * 2; z++) {
                    BlockPos currentPos = startingPos.offset(x, y, z);
                    if(posWithinRange(currentPos)) {
                        if(level.getBlockEntity(currentPos) instanceof PowerConsumer consumer)
                            consumer.getPosHolder().add(worldPosition); // Notify consumers that this altar exists.

                        addBlock(level.getBlockState(currentPos).getBlock());
                    }
                }
            }
        }
    }

    /**
     * Check if a given position is within this Altar's range.
     *
     * @return True if in range, otherwise false.
     */
    public boolean posWithinRange(BlockPos pos) {
        int range = ServerConfig.INSTANCE.altarRange.get();
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
     * Add a block to this {@link AltarBlockEntity}'s {@link AltarPowerData}.
     */
    public void addBlock(Block block) {
        powerData.addBlock(level, block);
        setChanged();
    }

    /**
     * Remove a block from this {@link AltarBlockEntity}'s {@link AltarPowerData}.
     */
    public void removeBlock(Block block) {
        powerData.removeBlock(level, block);
        setChanged();
    }

    /**
     * Add an upgrade to this {@link AltarBlockEntity}'s {@link AltarPowerData}.
     */
    public void addUpgrade(Block block) {
        powerData.addUpgrade(level, block);
        setChanged();
    }

    /**
     * Remove an upgrade from this {@link AltarBlockEntity}'s {@link AltarPowerData}.
     */
    public void removeUpgrade(Block block) {
        powerData.removeUpgrade(level, block);
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.enchanted.altar");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new AltarMenu(id, this, new NonSettableContainerData() {
            @Override
            public int get(int index) {
                return switch(index) {
                    case 0 -> (int)power;
                    case 1 -> (int)powerData.getCapacity();
                    case 2 -> (int)Math.round(powerData.getRechargeMultiplier() * 100); // Multiply by 100 to allow for 2 decimal places.
                    default -> throw new IllegalStateException("Unexpected altar container index: " + index);
                };
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    @Override
    public boolean tryConsume(double amount) {
        if(power > amount) {
            power -= amount;
            return true;
        }
        return false;
    }

}
