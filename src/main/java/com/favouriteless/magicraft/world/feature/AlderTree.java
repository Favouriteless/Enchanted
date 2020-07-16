package com.favouriteless.magicraft.world.feature;

import com.favouriteless.magicraft.init.MagicraftBlocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.AcaciaFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Random;

public class AlderTree extends Tree {

    public static final TreeFeatureConfig ALDER_TREE_CONFIG = (new TreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(MagicraftBlocks.ALDER_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(MagicraftBlocks.ALDER_LEAVES.get().getDefaultState()),
            new BlobFoliagePlacer(3, 0))
            .baseHeight(70)
            .heightRandA(1)
            .heightRandB(0)
            .setSapling((IPlantable) MagicraftBlocks.ALDER_SAPLING.get())
            .build()
    );


    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean b) {
        return Feature.FANCY_TREE.withConfiguration(ALDER_TREE_CONFIG);
    }
}
