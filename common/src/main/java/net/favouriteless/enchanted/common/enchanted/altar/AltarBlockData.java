package net.favouriteless.enchanted.common.enchanted.altar;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Iterator;

public class AltarBlockData {

    private boolean isInitialised = false;

    public Object2IntMap<Block> blockCounts = new Object2IntOpenHashMap<>();
    public Object2IntMap<TagKey<Block>> tagCounts = new Object2IntOpenHashMap<>();

    public int addBlock(Level level, Block block) {
        tryInitialise(level);
        return changePower(level, block, this::changeAdd);
    }

    public int removeBlock(Level level, Block block) {
        tryInitialise(level);
        return changePower(level, block, this::changeRemove);
    }

    @SuppressWarnings("deprecation")
    private int changePower(Level level, Block block, ApplyFunction apply) {
        PowerProvider provider = PowerProvider.get(level, block);
        if(provider != null)
            return apply.apply(blockCounts, block, provider);

        Iterator<TagKey<Block>> iterator = block.builtInRegistryHolder().tags().iterator();
        while(iterator.hasNext()) {
            TagKey<Block> tag = iterator.next();
            provider = PowerProvider.get(level, tag);

            if(provider != null)
                return apply.apply(tagCounts, tag, provider);
        }

        return 0;
    }

    private <T> int changeAdd(Object2IntMap<T> map, T key, PowerProvider provider) {
        return map.compute(key, (k, v) -> v != null ? v + 1 : 1) <= provider.limit() ? provider.power() : 0;
    }

    private <T> int changeRemove(Object2IntMap<T> map, T key, PowerProvider provider) {
        int count = map.compute(key, (k, v) -> v != null ? v - 1 : 0);
        if(count < 1)
            map.removeInt(key);
        return count < provider.limit() ? provider.power() : 0;
    }

    public double calculatePower(Level level, double powerMultiplier) {
        tryInitialise(level);
        double newPower = 0.0D;

        for(Block block : blockCounts.keySet()) {
            PowerProvider provider = PowerProvider.get(level, block);
            if(provider != null)
                newPower += Math.max(0, Math.min(provider.limit(), blockCounts.getInt(block))) * provider.power() * powerMultiplier;
        }

        for(TagKey<Block> tag : tagCounts.keySet()) {
            PowerProvider provider = PowerProvider.get(level, tag);
            if(provider != null)
                newPower += Math.max(0, Math.min(provider.limit(), tagCounts.getInt(tag))) * provider.power() * powerMultiplier;
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
            blockNbt.putInt(BuiltInRegistries.BLOCK.getKey(block).toString(), blockCounts.getInt(block));

        for(TagKey<Block> tag : tagCounts.keySet())
            tagNbt.putInt(tag.location().toString(), tagCounts.getInt(tag));

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
        if(isInitialised)
            return;

        for(Block block : blockCounts.keySet()) { // Remove old entries which no longer provide power.
            if(PowerProvider.get(level, block) == null)
                blockCounts.removeInt(block);
        }
        for(TagKey<Block> tag : tagCounts.keySet()) {
            if(PowerProvider.get(level, tag) == null)
                tagCounts.removeInt(tag);
        }
        isInitialised = true;
    }



    @FunctionalInterface
    public interface ApplyFunction {

        <T> int apply(Object2IntMap<T> map, T key, PowerProvider provider);

    }

}