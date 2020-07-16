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

public class RowanTree extends Tree {

    public static final TreeFeatureConfig ROWAN_TREE_CONFIG = (new TreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(MagicraftBlocks.ROWAN_LOG.get().getDefaultState()),
            new SimpleBlockStateProvider(MagicraftBlocks.ROWAN_LEAVES.get().getDefaultState()),
            new BlobFoliagePlacer(3, 0))
            .baseHeight(6)
            .heightRandA(1)
            .foliageHeight(3)
            .ignoreVines()
            .setSapling((IPlantable)MagicraftBlocks.ROWAN_SAPLING.get())).build();

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean b) {
        return Feature.NORMAL_TREE.withConfiguration(ROWAN_TREE_CONFIG);
    }
}
