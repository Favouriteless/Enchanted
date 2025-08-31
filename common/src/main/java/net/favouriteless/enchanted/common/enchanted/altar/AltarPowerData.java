package net.favouriteless.enchanted.common.enchanted.altar;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.favouriteless.enchanted.common.init.EData;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AltarPowerData {

    private double powerMultiplier; // Gets recalculated when upgrades change.
    private double rechargeMultiplier; // Gets recalculated when upgrades change.

    private int capacity = 0; // Raw capacity, no multipliers applied.

    private final Map<ResourceLocation, Object2IntOpenHashMap<AltarUpgrade>> upgrades = new HashMap<>();
    private final Object2IntMap<Block> blocks = new Object2IntOpenHashMap<>();
    private final Object2IntMap<TagKey<Block>> tags = new Object2IntOpenHashMap<>();

    /**
     * Attempt to add an upgrade block. If the block actually has an upgrade, power and recharge multipliers will be
     * recalculated.
     *
     * @param level {@link Level} to grab registries from.
     * @param block The {@link Block} to add.
     */
    public void addUpgrade(Level level, Block block) {
        AltarUpgrade upgrade = AltarUpgrade.get(level, block);
        if(upgrade == null)
            return;
        upgrades.computeIfAbsent(upgrade.type(), k -> new Object2IntOpenHashMap<>()).compute(upgrade, (k, v) -> v != null ? v+1 : 1);
        calculateUpgrades();
    }

    /**
     * Remove to remove an upgrade block. If the block actually has an upgrade, power and recharge multipliers will be
     * recalculated.
     *
     * @param level {@link Level} to grab registries from.
     * @param block The {@link Block} to remove.
     */
    public void removeUpgrade(Level level, Block block) {
        AltarUpgrade upgrade = AltarUpgrade.get(level, block);
        if(upgrade == null)
            return;

        Map<AltarUpgrade, Integer> type = upgrades.get(upgrade.type());
        if(type != null && type.containsKey(upgrade)) {
            int count = type.get(upgrade);
            if(count <= 1)
                type.remove(upgrade);
            else
                type.put(upgrade, count-1);
        }
        calculateUpgrades();
    }

    /**
     * Attempt to add a non-upgrade block.
     *
     * @param level {@link Level} to grab registries from.
     * @param block The {@link Block} to add.
     *
     * @return Amount of power added.
     */
    public void addBlock(Level level, Block block) {
        tryChangeBlock(level, block, this::applyAdd);
    }

    /**
     * Attempt to remove a non-upgrade block.
     *
     * @param level {@link Level} to grab registries from.
     * @param block The {@link Block} to remove.
     *
     * @return Amount of power removed.
     */
    public void removeBlock(Level level, Block block) {
        tryChangeBlock(level, block, this::applyRemove);
    }

    /**
     * Remove any invalid or zero entries and calculate capacity from after loading has occurred. Typically called from
     * a {@link BlockEntity} in its first tick
     */
    public void validate(Level level) {
        capacity = 0;

        blocks.object2IntEntrySet().removeIf(e -> e.getIntValue() == 0 || PowerProvider.get(level, e.getKey()) == null);
        tags.object2IntEntrySet().removeIf(e -> e.getIntValue() == 0 || PowerProvider.get(level, e.getKey()) == null);

        for(Block block : blocks.keySet()) {
            PowerProvider provider = PowerProvider.get(level, block);
            capacity += Math.min(blocks.getInt(block), provider.limit()) * provider.power();
        }
        for(TagKey<Block> tag : tags.keySet()) {
            PowerProvider provider = PowerProvider.get(level, tag);
            capacity += Math.min(tags.getInt(tag), provider.limit()) * provider.power();
        }
    }

    private void calculateUpgrades() {
        powerMultiplier = 0;
        rechargeMultiplier = 0;

        for(ResourceLocation type : upgrades.keySet()) {
            double highestPower = 0.0D;
            double highestRecharge = 0.0D;

            for(AltarUpgrade upgrade : upgrades.get(type).keySet()) {
                if(upgrade.power() > highestPower)
                    highestPower = upgrade.power();
                if(upgrade.recharge() > highestRecharge)
                    highestRecharge = upgrade.recharge();
            }

            powerMultiplier += highestPower;
            rechargeMultiplier += highestRecharge;
        }
    }

    @SuppressWarnings("deprecation")
    private void tryChangeBlock(Level level, Block block, ApplyFunction apply) {
        PowerProvider provider = PowerProvider.get(level, block);
        if(provider != null) {
            apply.apply(blocks, block, provider);
            return;
        }

        block.builtInRegistryHolder().tags()
                .map(tag -> Pair.of(tag, PowerProvider.get(level, tag)))
                .filter(pair -> pair.getSecond() != null)
                .findFirst()
                .ifPresent(pair -> apply.apply(tags, pair.getFirst(), pair.getSecond()));
    }

    private <T> void applyAdd(Object2IntMap<T> map, T key, PowerProvider provider) {
        int out = map.compute(key, (k, v) -> v != null ? v + 1 : 1) <= provider.limit() ? provider.power() : 0;
        capacity += out;
    }

    private <T> void applyRemove(Object2IntMap<T> map, T key, PowerProvider provider) {
        int count = map.compute(key, (k, v) -> v != null ? v - 1 : 0);
        if(count < 1)
            map.removeInt(key);
        int out = count < provider.limit() ? provider.power() : 0;
        capacity -= out;
    }

    public CompoundTag save(Level level) {
        CompoundTag out = new CompoundTag();

        CompoundTag upgradeTag = new CompoundTag();
        CompoundTag blockTag = new CompoundTag();
        CompoundTag tagTag = new CompoundTag();

        blocks.forEach((k, v) -> blockTag.putInt(BuiltInRegistries.BLOCK.getKey(k).toString(), v));
        tags.forEach((k, v) -> tagTag.putInt(k.location().toString(), v));

        Registry<AltarUpgrade> upgradeRegistry = level.registryAccess().registryOrThrow(EData.ALTAR_UPGRADE_REGISTRY);
        upgrades.values().forEach(m -> m.forEach((upg, val) -> {
            if(val == 0)
                return;
            ResourceLocation id = upgradeRegistry.getKey(upg);
            if(id != null)
                upgradeTag.putInt(id.toString(), val);
        }));


        out.put("upgrades", upgradeTag);
        out.put("blocks", blockTag);
        out.put("tags", tagTag);
        out.putInt("capacity", capacity);

        return out;
    }

    public void load(CompoundTag tag, Provider registries) {
        CompoundTag upgradeTag = tag.getCompound("upgrades");
        CompoundTag blockTag = tag.getCompound("blocks");
        CompoundTag tagTag = tag.getCompound("tags");

        for(String name : blockTag.getAllKeys()) {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(name));
            if(block != Blocks.AIR) // If AIR we'll assume the block doesn't exist.
                blocks.put(block, blockTag.getInt(name));
        }
        for(String name : tagTag.getAllKeys())
            tags.put(TagKey.create(Registries.BLOCK, ResourceLocation.parse(name)), tagTag.getInt(name));

        RegistryLookup<AltarUpgrade> lookup = registries.lookupOrThrow(EData.ALTAR_UPGRADE_REGISTRY);
        for(String name : upgradeTag.getAllKeys()) {
            Optional<Reference<AltarUpgrade>> ref = lookup.get(ResourceKey.create(EData.ALTAR_UPGRADE_REGISTRY, ResourceLocation.parse(name)));
            if(ref.isEmpty())
                continue;

            AltarUpgrade upg = ref.get().value();
            upgrades.computeIfAbsent(upg.type(), k -> new Object2IntOpenHashMap<>()).put(upg, upgradeTag.getInt(name));
        }
        capacity = tag.getInt("capacity"); // This is validated in the first tick
        calculateUpgrades();
    }

    public double getCapacity() {
        return capacity * powerMultiplier;
    }

    public double getRechargeMultiplier() {
        return rechargeMultiplier;
    }

    public void reset() {
        powerMultiplier = 0;
        rechargeMultiplier = 0;
        upgrades.clear();
        blocks.clear();
        tags.clear();
    }

    @FunctionalInterface
    public interface ApplyFunction {
        <T> void apply(Object2IntMap<T> map, T key, PowerProvider provider);
    }

}
