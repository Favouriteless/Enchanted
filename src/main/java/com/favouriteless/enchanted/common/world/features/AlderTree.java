package com.favouriteless.enchanted.common.world.features;

import com.favouriteless.enchanted.core.init.EnchantedBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.FancyFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.FancyTrunkPlacer;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;

public class AlderTree extends Tree {

    public static final BaseTreeFeatureConfig ALDER_TREE_CONFIG = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(EnchantedBlocks.ALDER_LOG.get().defaultBlockState()),
            new SimpleBlockStateProvider(EnchantedBlocks.ALDER_LEAVES.get().defaultBlockState()),
            new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(3), 3),
            new FancyTrunkPlacer(4, 6, 0),
            new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))
            .ignoreVines()
            .heightmap(Heightmap.Type.MOTION_BLOCKING)
            .build();

    @Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random pRandom, boolean pLargeHive) {
        return Feature.TREE.configured(ALDER_TREE_CONFIG);
    }
}