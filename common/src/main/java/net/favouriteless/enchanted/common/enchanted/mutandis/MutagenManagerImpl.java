package net.favouriteless.enchanted.common.enchanted.mutandis;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.favouriteless.enchanted.api.MutagenManager;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.enchanted.mutandis.MutagenInfo.MutagenSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutagenManagerImpl implements MutagenManager {

    public static final MutagenManagerImpl INSTANCE = new MutagenManagerImpl();

    private MutagenManagerImpl() {}

    @Override
    public boolean tryStartMutating(ServerLevel level, BlockPos pos, boolean extremis) {
        if(!isMutating(level, pos) && canMutate(level, level.getBlockState(pos).getBlock())) {
            MutagenSavedData.get(level).add(pos, extremis);
            return true;
        }
        return false;
    }

    @Override
    public boolean isMutating(ServerLevel level, BlockPos pos) {
        return MutagenSavedData.get(level).contains(pos);
    }

    @Override
    public boolean canMutate(ServerLevel level, Block block) {
        return level.registryAccess().registryOrThrow(EData.MUTAGEN_REGISTRY).containsKey(BuiltInRegistries.BLOCK.getKey(block));
    }

    @Override
    public Map<Block, List<MutagenSet>> getMutagensFor(Level level, Block result) {
        Map<Block, List<MutagenSet>> out = new HashMap<>();

        for(Map.Entry<ResourceKey<MutagenInfo>, MutagenInfo> entry : level.registryAccess().registryOrThrow(EData.MUTAGEN_REGISTRY).entrySet()) {
            Block mutee = BuiltInRegistries.BLOCK.get(entry.getKey().location());

            for(MutagenSet set : entry.getValue().sets()) {
                if(set.result() == result)
                    out.computeIfAbsent(mutee, l -> new ArrayList<>()).add(set);
            }
        }

        return out;
    }

    public boolean randomTick(ServerLevel level, BlockPos pos) {
        if(!canMutate(level, level.getBlockState(pos).getBlock()))
            return false;
        if(!isMutating(level, pos))
            return false;

        return tryMutate(level, pos);
    }

    private boolean tryMutate(ServerLevel level, BlockPos pos) {
        Registry<MutagenInfo> registry = level.registryAccess().registryOrThrow(EData.MUTAGEN_REGISTRY);
        Block block = level.getBlockState(pos).getBlock();

        MutagenInfo info = registry.get(BuiltInRegistries.BLOCK.getKey(block));
        if(info == null)
            return false;

        Object2IntMap<MutagenSet> counts = getValidMutagenSets(level, pos, info);
        MutagenSet set = getRandomWeighted(getWeightedBlocks(counts));
        if(set == null)
            return false;

        // 40 = max blocks, 7 = average ticks per full crop
        double chance = Math.min(counts.getInt(set), 40) / (40 * 7.0D) * 1.5D;
        if(Math.random() >= chance)
            return false;

        mutate(level, pos, set.result());
        return true;
    }

    private void mutate(ServerLevel level, BlockPos pos, Block newBlock) {
        BlockState state = level.getBlockState(pos);
        BlockState newState = newBlock.defaultBlockState();

        for(Property<?> property : state.getProperties()) {
            if(newState.hasProperty(property))
                newState = copyProperty(property, state, newState); // Attempt to capture and copy any viable properties.
        }

        level.setBlockAndUpdate(pos, newState);
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.MASTER);
        level.sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 25, 0.5D, 0.5D, 0.5D, 0.0D);
    }

    private <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState old, BlockState state) {
        return state.setValue(property, old.getValue(property));
    }

    private Object2IntMap<MutagenSet> getValidMutagenSets(ServerLevel level, BlockPos pos, MutagenInfo info) {
        Object2IntMap<MutagenSet> out = new Object2IntOpenHashMap<>(info.sets().size());
        boolean extremis = MutagenSavedData.get(level).isExtremis(pos);

        for(BlockPos p : BlockPos.betweenClosed(pos.getX()-2, pos.getY()-2, pos.getZ()-2, pos.getX()+2, pos.getY()+2, pos.getZ()+2)) {
            Block block = level.getBlockState(p).getBlock();

            for(MutagenSet set : info.sets()) {
                if(!set.mutagens().contains(block) || (set.extremis() && !extremis))
                    continue;
                out.put(set, out.getOrDefault(set, 0) + 1);
            }
        }
        return out;
    }

    private Object2DoubleMap<MutagenSet> getWeightedBlocks(Object2IntMap<MutagenSet> sets) {
        int total = sets.keySet().stream().mapToInt(MutagenSet::weight).sum();

        Object2DoubleMap<MutagenSet> out = new Object2DoubleOpenHashMap<>(sets.size());
        sets.forEach((set, i) -> out.put(set, i * set.weight() / (double)total));
        return out;
    }

    private MutagenSet getRandomWeighted(Object2DoubleMap<MutagenSet> weights) {
        List<Pair<MutagenSet, Double>> cumulativeWeights = new ArrayList<>(weights.size());
        double sum = 0.0D;

        for(MutagenSet set : weights.keySet()) {
            sum += weights.getDouble(set);
            cumulativeWeights.add(Pair.of(set, sum));
        }

        double rand = Math.random();
        for(Pair<MutagenSet, Double> pair : cumulativeWeights) {
            if(pair.getSecond() / sum > rand)
                return pair.getFirst();
        }
        return null;
    }

}
