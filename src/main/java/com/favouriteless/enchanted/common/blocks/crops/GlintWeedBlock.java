/*
 * Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class GlintWeedBlock extends AbstractSpreadingBlock {

    public GlintWeedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        BlockState state1 = pLevel.getBlockState(pPos.below());
        BlockState state2 = pLevel.getBlockState(pPos.above());

        return (!pLevel.isEmptyBlock(pPos.below()) && state1.isFaceSturdy(pLevel, pPos.below(), Direction.UP) || state1.is(BlockTags.LEAVES) ) ||
                (!pLevel.isEmptyBlock(pPos.above()) && state2.isFaceSturdy(pLevel, pPos.above(), Direction.DOWN) || state2.is(BlockTags.LEAVES) );
    }

    @Override
    public boolean canSpreadOn(Block block) {
        return block.is(Blocks.GRASS_BLOCK) || block.is(Blocks.DIRT) || block.is(Blocks.SAND);
    }
}
