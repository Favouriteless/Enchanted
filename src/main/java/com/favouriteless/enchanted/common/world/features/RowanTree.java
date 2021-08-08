package com.favouriteless.enchanted.common.world.features;

import com.favouriteless.enchanted.core.init.EnchantedBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class RowanTree extends Tree {

    public static final BaseTreeFeatureConfig ROWAN_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(EnchantedBlocks.ROWAN_LOG.get().defaultBlockState()),
            new SimpleBlockStateProvider(EnchantedBlocks.ROWAN_LEAVES.get().defaultBlockState()),
            new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1)))
            .ignoreVines()
            .build();

    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.configured(ROWAN_TREE_CONFIG);
    }

}