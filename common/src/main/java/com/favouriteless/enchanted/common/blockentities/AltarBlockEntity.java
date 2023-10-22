package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.ISerializable;
import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.api.power.IPowerProvider;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.altar.AltarPowerProvider;
import com.favouriteless.enchanted.common.altar.AltarStateObserver;
import com.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import com.favouriteless.enchanted.common.init.EnchantedData;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.menus.AltarMenu;
import com.favouriteless.stateobserver.StateObserverManager;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AltarBlockEntity extends BlockEntity implements MenuProvider, IPowerProvider {

    private final double rechargeRate = AutoConfig.getConfigHolder(CommonConfig.class).getConfig().altarBaseRecharge;
    private final AltarBlockData altarBlockData = new AltarBlockData();
    private final AltarUpgradeData altarUpgradeData = new AltarUpgradeData();
    private double rechargeMultiplier = 1.0D;
    private double powerMultiplier = 1.0D;
    private double maxPower;
    private double currentPower;

    private final ContainerData fields = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch(pIndex) {
                case 0 -> (int) Math.round(currentPower);
                case 1 -> (int) Math.round(maxPower);
                case 2 -> (int) Math.round(rechargeMultiplier);
                default -> 0;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch(pIndex) {
                case 0:
                    currentPower = pValue;
                case 1:
                    maxPower = pValue;
                case 2:
                    rechargeMultiplier = pValue;
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

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.ALTAR.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T t) {
        if(t instanceof AltarBlockEntity blockEntity) {
            if(level != null && !level.isClientSide) {
                if(blockEntity.ticksAlive % 20 == 0) {
                    blockEntity.stateObserver.checkChanges();
                }
                if(blockEntity.currentPower <= blockEntity.maxPower)
                    blockEntity.currentPower += blockEntity.rechargeRate * blockEntity.rechargeMultiplier;
                if(blockEntity.currentPower > blockEntity.maxPower)
                    blockEntity.currentPower = blockEntity.maxPower;
                blockEntity.ticksAlive++;
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(!level.isClientSide) {
            if(stateObserver == null)
                stateObserver = StateObserverManager.getObserver(level, worldPosition, AltarStateObserver.class);
            if(stateObserver == null) {
                int range = AutoConfig.getConfigHolder(CommonConfig.class).getConfig().altarRange;
                stateObserver = StateObserverManager.createObserver(new AltarStateObserver(level, worldPosition, range + 4, range + 4, range + 4));
            }
            facingX = level.getBlockState(worldPosition).getValue(AltarBlock.FACING_X);
            centerPos = facingX ?
                    Vec3.atLowerCornerOf(worldPosition).add(1.0D, 0.0D, 0.5D) :
                    Vec3.atLowerCornerOf(worldPosition).add(0.5D, 0.0D, 1.0D);
            if(firstLoad)
                recalculatePower();
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("currentPower", currentPower);
        nbt.putDouble("maxPower", maxPower);
        nbt.putDouble("powerMultiplier", powerMultiplier);
        nbt.putDouble("rechargeMultiplier", rechargeMultiplier);
        nbt.put("blockData", altarBlockData.serialize());
        nbt.put("upgradeData", altarUpgradeData.serialize());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        setMaxPower(nbt.getDouble("maxPower"));
        setMaxPower(nbt.getDouble("maxPower"));
        currentPower = nbt.getDouble("currentPower");
        powerMultiplier = nbt.getDouble("powerMultiplier");
        rechargeMultiplier = nbt.getDouble("rechargeMultiplier");
        if(nbt.contains("blockData") && nbt.contains("upgradeData")) {
            altarBlockData.deserialize((CompoundTag) nbt.get("blockData"));
            altarUpgradeData.deserialize((CompoundTag) nbt.get("upgradeData"));
        }
        else
            Enchanted.LOG.info(String.format("Failed to load power block data for altar at x:%s y:%s z:%s", getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()));

        firstLoad = false;
    }

    private void recalculatePower() {
        recalculateUpgrades();
        recalculateBlocks();
    }

    private void recalculateUpgrades() {
        if(level != null && !level.isClientSide) {
            BlockPos minPos = worldPosition.above();
            BlockPos maxPos = facingX ? minPos.offset(2, 0, 1) : minPos.offset(1, 0, 2);

            altarUpgradeData.reset();
            for(BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                altarUpgradeData.addBlock(level.getBlockState(pos).getBlock());
            }
            powerMultiplier = altarUpgradeData.calculatePowerMultiplier();
            rechargeMultiplier = altarUpgradeData.calculateRechargeMultiplier();
            setChanged();
        }
    }

    private void recalculateBlocks() {
        if(level != null && !level.isClientSide) {
            int range = AutoConfig.getConfigHolder(CommonConfig.class).getConfig().altarRange;
            BlockPos startingPos = facingX ?
                    new BlockPos(centerPos.add(-(range+4), -(range+2), -(range+2))) :
                    new BlockPos(centerPos.add(-(range+2), -(range+2), -(range+4)));

            altarBlockData.reset();
            for (int x = 0; x < (range+2)*2; x++) {
                for (int y = 0; y < (range+2)*2; y++) {
                    for (int z = 0; z < (range+2)*2; z++) {
                        BlockPos currentPos = startingPos.offset(x, y, z);

                        if(posWithinRange(currentPos, range)) {
                            if(level.getBlockEntity(currentPos) instanceof IPowerConsumer consumer)
                                consumer.getPosHolder().add(worldPosition); // Notify consumers that this altar exists.

                            addBlock(level.getBlockState(currentPos).getBlock());
                        }
                    }
                }
            }
        }
    }

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

    public double distanceTo(BlockPos pos) {
        double dx = pos.getX() - centerPos.x;
        double dy = pos.getY() - centerPos.y;
        double dz = pos.getZ() - centerPos.z;
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

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

    public void setMaxPower(double power) {
        maxPower = power;
        if(currentPower > maxPower) currentPower = maxPower;
        this.setChanged();
    }

    public void addBlock(Block block) {
        maxPower += altarBlockData.addBlock(block) * powerMultiplier;
        setChanged();
    }

    public void removeBlock(Block block) {
        maxPower -= altarBlockData.removeBlock(block) * powerMultiplier;
        if(currentPower > maxPower) currentPower = maxPower;
        setChanged();
    }

    public void addUpgrade(Block block) {
        if(altarUpgradeData.addBlock(block)) {
            double newPowerMultiplier = altarUpgradeData.calculatePowerMultiplier();
            if(newPowerMultiplier != powerMultiplier) {
                powerMultiplier = newPowerMultiplier;
                setMaxPower(altarBlockData.calculatePower(powerMultiplier));
            }
            rechargeMultiplier = altarUpgradeData.calculateRechargeMultiplier();
            setChanged();
        }
    }

    public void removeUpgrade(Block block) {
        if(altarUpgradeData.removeBlock(block)) {
            double newPowerMultiplier = altarUpgradeData.calculatePowerMultiplier();
            if(newPowerMultiplier != powerMultiplier) {
                powerMultiplier = newPowerMultiplier;
                setMaxPower(altarBlockData.calculatePower(powerMultiplier));
            }
            rechargeMultiplier = altarUpgradeData.calculateRechargeMultiplier();
            setChanged();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.enchanted.altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new AltarMenu(id, this, fields);
    }

    @Override
    public boolean tryConsumePower(double amount) {
        if(this.currentPower > amount) {
            currentPower -= amount;
            return true;
        }
        return false;
    }

    public static class AltarBlockData implements ISerializable<CompoundTag> {
        public Map<Block, Integer> blockCounts = new HashMap<>();
        public Map<TagKey<Block>, Integer> tagCounts = new HashMap<>();

        public AltarBlockData() {
            for(AltarPowerProvider<Block> provider : EnchantedData.POWER_BLOCKS.getAll())
                blockCounts.put(provider.getKey(), 0);
            for(AltarPowerProvider<TagKey<Block>> provider : EnchantedData.POWER_TAGS.getAll())
                tagCounts.put(provider.getKey(), 0);
        }

        public CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();
            CompoundTag blockNbt = new CompoundTag();
            CompoundTag tagNbt = new CompoundTag();

            for(Block block : blockCounts.keySet()) {
                blockNbt.putInt(Registry.BLOCK.getKey(block).toString(), blockCounts.get(block));
            }
            for(TagKey<Block> tag : tagCounts.keySet()) {
                tagNbt.putInt(tag.location().toString(), tagCounts.get(tag));
            }

            nbt.put("blocksAmount", blockNbt);
            nbt.put("tagsAmount", tagNbt);
            return nbt;
        }

        public void deserialize(CompoundTag nbt) {
            CompoundTag blockNbt = (CompoundTag)nbt.get("blocksAmount");
            CompoundTag tagNbt = (CompoundTag)nbt.get("tagsAmount");

            for(String name : blockNbt.getAllKeys()) {
                blockCounts.put(Registry.BLOCK.get(new ResourceLocation(name)), blockNbt.getInt(name));
            }
            for(String name : tagNbt.getAllKeys()) {
                tagCounts.put(EnchantedTags.createBlockTag(new ResourceLocation(name)), tagNbt.getInt(name));
            }

        }

        public double addBlock(Block block) {
            Integer amount = blockCounts.get(block);

            if(amount == null) { // Not in blocks
                for(AltarPowerProvider<TagKey<Block>> provider : EnchantedData.POWER_TAGS.getAll()) { // For all tag power providers
                    if(ForgeRegistries.BLOCKS.tags().getTag(provider.getKey()).contains(block)) { // If block part of provider tag
                        amount = tagCounts.get(provider.getKey());
                        tagCounts.replace(provider.getKey(), tagCounts.get(provider.getKey()) + 1);
                        return amount < provider.getLimit() ? provider.getPower() : 0; // Return 0 if above limit
                    }
                }
            }
            if(amount == null) { // Not in blocks OR tags
                return 0;
            }

            AltarPowerProvider<Block> provider = EnchantedData.POWER_BLOCKS.get(block);
            blockCounts.replace(block, amount + 1);
            return amount < provider.getLimit() ? provider.getPower() : 0; // Return 0 if above limit
        }

        public double removeBlock(Block block) {
            Integer amount = blockCounts.get(block);

            if(amount == null) { // Not in blocks
                for(AltarPowerProvider<TagKey<Block>> provider : EnchantedData.POWER_TAGS.getAll()) { // For all tag power providers
                    if(ForgeRegistries.BLOCKS.tags().getTag(provider.getKey()).contains(block)) { // If block part of provider tag
                        amount = tagCounts.get(provider.getKey());
                        tagCounts.replace(provider.getKey(), tagCounts.get(provider.getKey()) - 1);

                        return amount > provider.getLimit() ? 0 : provider.getPower(); // Return 0 if above limit
                    }
                }
            }
            if(amount == null) { // Not in blocks or tags
                return 0;
            }

            AltarPowerProvider<Block> provider = EnchantedData.POWER_BLOCKS.get(block);
            blockCounts.replace(block, amount - 1);
            return amount > provider.getLimit() ? 0 : provider.getPower(); // Return 0 if above limit
        }

        public double calculatePower(double powerMultiplier) {
            double newPower = 0.0D;

            for(Block block : blockCounts.keySet()) {
                AltarPowerProvider<Block> powerProvider = EnchantedData.POWER_BLOCKS.get(block);
                if(powerProvider == null) {
                    blockCounts.remove(powerProvider.getKey());
                    break;
                }
                newPower += Math.max(0, Math.min(powerProvider.getLimit(), blockCounts.get(block))) * powerProvider.getPower() * powerMultiplier;
            }
            for(TagKey<Block> tag : tagCounts.keySet()) {
                AltarPowerProvider<TagKey<Block>> powerProvider = EnchantedData.POWER_TAGS.get(tag);
                if(powerProvider == null) {
                    tagCounts.remove(powerProvider.getKey());
                    break;
                }
                newPower += Math.max(0, Math.min(powerProvider.getLimit(), tagCounts.get(tag))) * powerProvider.getPower() * powerMultiplier;
            }

            return newPower;
        }

        public void reset() {
            blockCounts.replaceAll((key, value) -> value = 0);
            tagCounts.replaceAll((key, value) -> value = 0);
        }

    }

    public static class AltarUpgradeData implements ISerializable<CompoundTag> {
        public final Map<String, Map<AltarUpgrade, Integer>> upgradesByType = new HashMap<>();

        public AltarUpgradeData() {
            for(AltarUpgrade upgrade : EnchantedData.ALTAR_UPGRADES.getAll()) {
                String type = upgrade.type().toString();
                if(!upgradesByType.containsKey(type))
                    upgradesByType.put(type, new HashMap<>());
                Map<AltarUpgrade, Integer> upgrades = upgradesByType.get(type);
                if(!upgrades.containsKey(upgrade))
                    upgrades.put(upgrade, 0);
            }
        }

        public CompoundTag serialize() {
            CompoundTag nbt = new CompoundTag();

            for(String type : upgradesByType.keySet()) {
                CompoundTag typeTag = new CompoundTag();

                Map<AltarUpgrade, Integer> map = upgradesByType.get(type);
                for(AltarUpgrade upgrade : map.keySet()) {
                    typeTag.putInt(Registry.BLOCK.getKey(upgrade.block()).toString(), map.get(upgrade));
                }
                nbt.put(type, typeTag);
            }
            return nbt;
        }

        public void deserialize(CompoundTag nbt) {
            for(String type : nbt.getAllKeys()) {
                upgradesByType.get(type).replaceAll((key, value) -> nbt.getInt(Registry.BLOCK.getKey(key.block()).toString()));
            }
        }

        public boolean addBlock(Block block) {
            AltarUpgrade upgrade = getUpgrade(block);
            if(upgrade == null)
                return false;

            String type = upgrade.type().toString();
            upgradesByType.get(type).put(upgrade, upgradesByType.get(type).get(upgrade)+1);
            return true;
        }

        public boolean removeBlock(Block block) {
            AltarUpgrade upgrade = getUpgrade(block);
            if(upgrade == null)
                return false;

            String type = upgrade.type().toString();
            int count = upgradesByType.get(type).get(upgrade);
            if(count > 0)
                upgradesByType.get(type).put(upgrade, count-1);
            return true;
        }

        private AltarUpgrade getUpgrade(Block block) {
            for(AltarUpgrade upgrade : EnchantedData.ALTAR_UPGRADES.getAll()) {
                if(upgrade.block() == block)
                    return upgrade;
            }
            return null;
        }

        public double calculatePowerMultiplier() {
            double multiplier = 1.0D;
            for(String type : upgradesByType.keySet()) {
                AltarUpgrade highestPriority = null;

                Map<AltarUpgrade, Integer> map = upgradesByType.get(type);
                for(AltarUpgrade upgrade : map.keySet()) {
                    if(map.get(upgrade) > 0) { // If has upgrade
                        if(highestPriority == null)
                            highestPriority = upgrade;
                        else if(highestPriority.priority() < upgrade.priority())
                            highestPriority = upgrade;
                    }
                }

                if(highestPriority != null) {
                    multiplier += highestPriority.powerMultiplier();
                }
            }
            return multiplier;
        }

        public double calculateRechargeMultiplier() {
            double multiplier = 1.0D;
            for(String type : upgradesByType.keySet()) {
                AltarUpgrade highestPriority = null;

                Map<AltarUpgrade, Integer> map = upgradesByType.get(type);
                for(AltarUpgrade upgrade : map.keySet()) {
                    if(map.get(upgrade) > 0) { // If has upgrade
                        if(highestPriority == null)
                            highestPriority = upgrade;
                        else if(highestPriority.priority() < upgrade.priority())
                            highestPriority = upgrade;
                    }
                }

                if(highestPriority != null) {
                    multiplier += highestPriority.rechargeMultiplier();
                }
            }
            return multiplier;
        }

        public void reset() {
            for(Map<AltarUpgrade, Integer> map : upgradesByType.values()) {
                map.replaceAll((key, value) -> value = 0);
            }
        }

    }

    public record AltarUpgrade(ResourceLocation type, Block block, double rechargeMultiplier, double powerMultiplier, int priority) { }

}
