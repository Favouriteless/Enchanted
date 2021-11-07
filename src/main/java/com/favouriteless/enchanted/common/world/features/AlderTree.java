/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.world.features;

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
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