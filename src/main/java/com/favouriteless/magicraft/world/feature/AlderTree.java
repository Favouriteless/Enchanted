package com.favouriteless.magicraft.world.feature;

import com.favouriteless.magicraft.init.MagicraftBlocks;
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
            new SimpleBlockStateProvider(MagicraftBlocks.ALDER_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(MagicraftBlocks.ALDER_LEAVES.get().getDefaultState()),
            new FancyFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(3), 3),
            new FancyTrunkPlacer(4, 6, 0),
            new TwoLayerFeature(0, 0, 0, OptionalInt.of(4)))
            .setIgnoreVines()
            .func_236702_a_(Heightmap.Type.MOTION_BLOCKING)
            .build();

    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.withConfiguration(ALDER_TREE_CONFIG);
    }

}