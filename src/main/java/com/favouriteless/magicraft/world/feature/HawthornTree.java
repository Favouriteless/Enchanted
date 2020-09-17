package com.favouriteless.magicraft.world.feature;

import com.favouriteless.magicraft.init.MagicraftBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import javax.annotation.Nullable;
import java.util.Random;

public class HawthornTree extends Tree {

    public static final BaseTreeFeatureConfig HAWTHORN_TREE_CONFIG = (new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(MagicraftBlocks.HAWTHORN_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(MagicraftBlocks.HAWTHORN_LEAVES.get().getDefaultState()),
            new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1)))
            .setIgnoreVines()
            .build();

    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.withConfiguration(HAWTHORN_TREE_CONFIG);
    }

}