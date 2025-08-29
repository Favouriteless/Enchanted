package net.favouriteless.enchanted.common.enchanted.altar;

import net.favouriteless.enchanted.common.init.EData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AltarUpgradeData {

    // UpgradeData gets loaded using loadData map because level access is needed to grab the actual upgrades.
    // The actual upgrade data should get constructed when the data is first accessed.
    private final Map<ResourceLocation, Integer> loadData = new HashMap<>();
    private boolean isInitialised = false;

    private final Map<ResourceLocation, Map<AltarUpgrade, Integer>> upgrades = new HashMap<>(); // Key is type, not upgrade location.

    /**
     * Add a {@link Block} to this {@link AltarUpgradeData}.
     *
     * @param level {@link Level} to grab registries from.
     * @param block The {@link Block} to add.
     *
     * @return true if block had an {@link AltarUpgrade} associated with it (stats should be recalculated).
     */
    public boolean addBlock(Level level, Block block) {
        tryInitialise(level);
        AltarUpgrade upgrade = AltarUpgrade.get(level, block);
        if(upgrade == null)
            return false;

        Map<AltarUpgrade, Integer> type = upgrades.computeIfAbsent(upgrade.type(), k -> new HashMap<>());
        type.put(upgrade, type.containsKey(upgrade) ? type.get(upgrade)+1 : 1);
        return true;
    }

    /**
     * Remove a {@link Block} from this {@link AltarUpgradeData}.
     *
     * @param level {@link Level} to grab registries from.
     * @param block The {@link Block} to remove.
     *
     * @return true if block had an {@link AltarUpgrade} associated with it (stats should be recalculated).
     */
    public boolean removeBlock(Level level, Block block) {
        tryInitialise(level);
        AltarUpgrade upgrade = AltarUpgrade.get(level, block);
        if(upgrade == null)
            return false;

        Map<AltarUpgrade, Integer> type = upgrades.get(upgrade.type());
        if(type != null && type.containsKey(upgrade)) {
            int count = type.get(upgrade);
            if(count <= 1)
                type.remove(upgrade);
            else
                type.put(upgrade, count-1);
        }
        return true;
    }

    public double calculatePowerMultiplier(Level level) {
        tryInitialise(level);
        double multiplier = 1.0D;

        for(ResourceLocation type : upgrades.keySet()) {
            AltarUpgrade highest = null;

            for(AltarUpgrade upgrade : upgrades.get(type).keySet()) {
                if(highest == null || highest.priority() < upgrade.priority())
                    highest = upgrade;
            }

            if(highest != null)
                multiplier += highest.power();
        }
        return multiplier;
    }

    public double calculateRechargeMultiplier(Level level) {
        tryInitialise(level);
        double multiplier = 1.0D;

        for(ResourceLocation type : upgrades.keySet()) {
            AltarUpgrade highest = null;

            for(AltarUpgrade upgrade : upgrades.get(type).keySet()) {
                if(highest == null || highest.priority() < upgrade.priority())
                    highest = upgrade;
            }

            if(highest != null)
                multiplier += highest.recharge();
        }
        return multiplier;
    }

    public void reset() {
        loadData.clear();
        upgrades.clear();
    }

    // When saving, the upgrades map gets flattened.
    public CompoundTag save(Level level) {
        CompoundTag nbt = new CompoundTag();
        level.registryAccess().registry(EData.ALTAR_UPGRADE_REGISTRY).ifPresent(registry -> {
            for(Map<AltarUpgrade, Integer> typeMap : upgrades.values()) {
                for(Entry<AltarUpgrade, Integer> entry : typeMap.entrySet())
                    nbt.putInt(registry.getKey(entry.getKey()).toString(), entry.getValue());
            }
        });

        return nbt;
    }

    public void load(CompoundTag nbt) {
        for(String key : nbt.getAllKeys())
            loadData.put(ResourceLocation.parse(key), nbt.getInt(key));
        isInitialised = false;
    }

    private void tryInitialise(Level level) {
        if(!isInitialised) {
            upgrades.clear();
            level.registryAccess().registry(EData.ALTAR_UPGRADE_REGISTRY).ifPresent(registry -> {

                for(Entry<ResourceLocation, Integer> entry : loadData.entrySet()) {
                    if(entry.getValue() == 0) // Do not add upgrades without a count.
                        continue;
                    AltarUpgrade upgrade = registry.get(entry.getKey());

                    if(upgrade != null) { // Upgrades which don't exist any more get discarded.
                        ResourceLocation type = upgrade.type();
                        Map<AltarUpgrade, Integer> typeMap = upgrades.computeIfAbsent(type, a -> new HashMap<>());

                        typeMap.put(upgrade, entry.getValue());
                    }
                }
                isInitialised = true;
            });
        }
    }

}