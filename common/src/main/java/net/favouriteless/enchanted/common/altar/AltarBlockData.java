package net.favouriteless.enchanted.common.altar;

import net.favouriteless.enchanted.common.init.EData;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AltarBlockData {

    private boolean isInitialised = false;

    public Map<Block, Integer> blockCounts = new HashMap<>();
    public Map<TagKey<Block>, Integer> tagCounts = new HashMap<>();

    public int addBlock(Level level, Block block) {
        tryInitialise(level);
        PowerProvider<Block> blockProvider = PowerProvider.getBlock(level, block);
        if(blockProvider != null) { // Block has a power associated with it specifically.
            int count = blockCounts.computeIfAbsent(block, k -> 0);
            blockCounts.put(block, count + 1);
            return count < blockProvider.limit() ? blockProvider.power() : 0;
        }

        Optional<Registry<PowerProvider<TagKey<Block>>>> registry = level.registryAccess().registry(EData.ALTAR_TAG_REGISTRY);
        if(registry.isPresent()) { // Block has a power tag associated with it.
            for(PowerProvider<TagKey<Block>> provider : registry.get()) {
                if(block.builtInRegistryHolder().is(provider.key())) {
                    int count = tagCounts.computeIfAbsent(provider.key(), k -> 0);
                    tagCounts.put(provider.key(), count + 1);
                    return count < provider.limit() ? provider.power() : 0;
                }
            }
        }

        return 0;
    }

    public int removeBlock(Level level, Block block) {
        tryInitialise(level);
        PowerProvider<Block> blockProvider = PowerProvider.getBlock(level, block);
        if(blockProvider != null) { // Block has a power associated with it specifically.
            int count = blockCounts.computeIfAbsent(block, k -> 0);
            if(count == 1)
                blockCounts.remove(block);
            else
                blockCounts.put(block, count - 1);
            return count > blockProvider.limit() ? 0 : blockProvider.power();
        }

        Optional<Registry<PowerProvider<TagKey<Block>>>> registry = level.registryAccess().registry(EData.ALTAR_TAG_REGISTRY);
        if(registry.isPresent()) { // Block has a power tag associated with it.
            for(PowerProvider<TagKey<Block>> provider : registry.get()) {
                if(block.builtInRegistryHolder().is(provider.key())) {
                    int count = tagCounts.computeIfAbsent(provider.key(), k -> 0);
                    if(count == 1)
                        tagCounts.remove(provider.key());
                    else
                        tagCounts.put(provider.key(), count - 1);
                    return count > provider.limit() ? 0 : provider.power();
                }
            }
        }

        return 0;
    }

    public double calculatePower(Level level, double powerMultiplier) {
        tryInitialise(level);
        double newPower = 0.0D;

        for(Block block : blockCounts.keySet()) {
            PowerProvider<Block> provider = PowerProvider.getBlock(level, block);
            if(provider != null)
                newPower += Math.max(0, Math.min(provider.limit(), blockCounts.get(block))) * provider.power() * powerMultiplier;
        }

        for(TagKey<Block> tag : tagCounts.keySet()) {
            PowerProvider<TagKey<Block>> provider = PowerProvider.getTag(level, tag);
            if(provider != null)
                newPower += Math.max(0, Math.min(provider.limit(), tagCounts.get(tag))) * provider.power() * powerMultiplier;
        }

        return newPower;
    }

    public void reset() {
        blockCounts.clear();
        tagCounts.clear();
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();
        CompoundTag blockNbt = new CompoundTag();
        CompoundTag tagNbt = new CompoundTag();

        for(Block block : blockCounts.keySet())
            blockNbt.putInt(BuiltInRegistries.BLOCK.getKey(block).toString(), blockCounts.get(block));

        for(TagKey<Block> tag : tagCounts.keySet())
            tagNbt.putInt(tag.location().toString(), tagCounts.get(tag));

        nbt.put("blockCounts", blockNbt);
        nbt.put("tagsCounts", tagNbt);
        return nbt;
    }

    public void load(CompoundTag nbt) {
        CompoundTag blockNbt = nbt.getCompound("blockCounts");
        CompoundTag tagNbt = nbt.getCompound("tagsCounts");

        for(String name : blockNbt.getAllKeys()) {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(name));
            if(block != Blocks.AIR) // If AIR we'll assume the block doesn't exist.
                blockCounts.put(block, blockNbt.getInt(name));
        }
        for(String name : tagNbt.getAllKeys())
            tagCounts.put(TagKey.create(Registries.BLOCK, ResourceLocation.parse(name)), tagNbt.getInt(name));

        isInitialised = false;
    }

    private void tryInitialise(Level level) {
        if(!isInitialised) { // Remove old entries which no longer provide power.
            for(Block block : blockCounts.keySet()) {
                if(PowerProvider.getBlock(level, block) == null)
                    blockCounts.remove(block);
            }
            for(TagKey<Block> tag : tagCounts.keySet()) {
                if(PowerProvider.getTag(level, tag) == null)
                    tagCounts.remove(tag);
            }
            isInitialised = true;
        }
    }


}