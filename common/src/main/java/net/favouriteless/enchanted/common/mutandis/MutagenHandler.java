package net.favouriteless.enchanted.common.mutandis;

import it.unimi.dsi.fastutil.Pair;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EData;
import net.favouriteless.enchanted.common.mutandis.MutagenInfo.MutagenSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutagenHandler {

    public static void tryMutate(ServerLevel level, BlockPos pos) {
        Registry<MutagenInfo> registry = level.registryAccess().registryOrThrow(EData.MUTAGEN_REGISTRY);
        Block block = level.getBlockState(pos).getBlock();

        MutagenInfo info = registry.get(BuiltInRegistries.BLOCK.getKey(block));
        if(info == null)
            return;


        int ember = 0;
        int spanish = 0;
        for(int i = 0; i < 10000; i++) {
            MutagenSet result = getRandomWeighted(applyWeights(getMutagenSets(level, pos, info)));
            if(result.weight() == 15)
                ember++;
            else
                spanish++;
        }

        Enchanted.LOG.info("Ember: {} | {}%         Spanish: {} | {}%", ember, ember / 100.0D, spanish, spanish / 100.0D);
    }

    protected static Map<MutagenSet, Double> getMutagenSets(ServerLevel level, BlockPos plant, MutagenInfo info) {
        Map<MutagenSet, Double> out = new HashMap<>();

        for(BlockPos pos : BlockPos.betweenClosed(plant.getX()-2, plant.getY()-2, plant.getZ()-2, plant.getX()+2, plant.getY()+2, plant.getZ()+2)) {
            Block block = level.getBlockState(pos).getBlock();

            for(MutagenSet set : info.mutagenSets().values()) {
                if(!set.mutagens().contains(block))
                    continue;
                out.put(set, out.getOrDefault(set, 0.0D) + 1.0D);
            }
        }
        return out;
    }

    protected static Map<MutagenSet, Double> applyWeights(Map<MutagenSet, Double> sets) {
        int total = 0;
        for(MutagenSet set : sets.keySet())
            total += set.weight();

        for(MutagenSet set : sets.keySet()) {
            sets.put(set, sets.get(set) * set.weight() / total);
        }
        return sets;
    }

    protected static MutagenSet getRandomWeighted(Map<MutagenSet, Double> sets) {
        List<Pair<MutagenSet, Double>> cumulativeWeights = new ArrayList<>();
        double sum = 0.0D;

        for(MutagenSet set : sets.keySet()) {
            sum += sets.get(set);
            cumulativeWeights.add(Pair.of(set, sum));
        }

        double rand = Math.random();
        for(Pair<MutagenSet, Double> pair : cumulativeWeights) {
            if(pair.right() / sum > rand)
                return pair.left();
        }
        return null; // This should never be reached.
    }

}
