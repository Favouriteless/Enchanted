/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.world.features;

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;

import javax.annotation.Nullable;
import java.util.OptionalInt;
import java.util.Random;

import net.minecraft.util.UniformInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;

public class HawthornTree extends AbstractTreeGrower {

    public static final TreeConfiguration HAWTHORN_TREE_CONFIG = new TreeConfiguration.TreeConfigurationBuilder(
            new SimpleStateProvider(EnchantedBlocks.HAWTHORN_LOG.get().defaultBlockState()),
            new SimpleStateProvider(EnchantedBlocks.HAWTHORN_LEAVES.get().defaultBlockState()),
            new FancyFoliagePlacer(UniformInt.fixed(2), UniformInt.fixed(4), 4),
            new FancyTrunkPlacer(3, 11, 0),
            new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4)))
            .ignoreVines()
            .heightmap(Heightmap.Types.MOTION_BLOCKING)
            .build();


    @Nullable
    protected ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random randomIn, boolean largeHive) {
        return Feature.TREE.configured(HAWTHORN_TREE_CONFIG);
    }

}