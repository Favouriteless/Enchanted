package com.favouriteless.magicraft.world.feature;

import com.favouriteless.magicraft.init.MagicraftBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

public class HawthornTree extends Tree {

    public static final TreeFeatureConfig HAWTHORN_TREE_CONFIG = (new TreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(MagicraftBlocks.HAWTHORN_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(MagicraftBlocks.HAWTHORN_LEAVES.get().getDefaultState()),
            new BlobFoliagePlacer(3, 0)))
            .baseHeight(9)
            .heightRandA(2)
            .heightRandB(2)
            .foliageHeightRandom(2)
            .trunkHeight(9)
            .setSapling((IPlantable) MagicraftBlocks.HAWTHORN_SAPLING.get())
            .build();




    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean b) {
        return Feature.FANCY_TREE.withConfiguration(HAWTHORN_TREE_CONFIG);
    }
}
