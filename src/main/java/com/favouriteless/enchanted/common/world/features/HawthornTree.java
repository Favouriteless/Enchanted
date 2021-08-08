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

public class HawthornTree extends Tree {

    public static final BaseTreeFeatureConfig HAWTHORN_TREE_CONFIG = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(EnchantedBlocks.HAWTHORN_LOG.get().defaultBlockState()),
            new SimpleBlockStateProvider(EnchantedBlocks.HAWTHORN_LEAVES.get().defaultBlockState()),
            new FancyFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(4), 4),
            new FancyTrunkPlacer(3, 11, 0),
            new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))
            .ignoreVines()
            .heightmap(Heightmap.Type.MOTION_BLOCKING)
            .build();


    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.configured(HAWTHORN_TREE_CONFIG);
    }

}