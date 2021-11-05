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

package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GlintWeedBlock extends BushBlock {

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
    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.random.nextInt(6) == 0 && pLevel.isAreaLoaded(pPos, 4)) {
            BlockPos newPos = pPos.offset(new BlockPos(
                    RANDOM.nextInt(7) - 3,
                    RANDOM.nextInt(7) - 3,
                    RANDOM.nextInt(7) - 3));

            if(pLevel.isEmptyBlock(newPos) && this.canSurvive(pState, pLevel, newPos)) {
                pLevel.setBlockAndUpdate(newPos, pState);
            }
        }

        if (pRandom.nextInt(14) == 0) {

            int weedLimit = 5;
            int j = 4;
            for(BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, -1, -4), pPos.offset(4, 1, 4))) {
                if (pLevel.getBlockState(blockpos).is(this)) {
                    weedLimit--;
                    if (weedLimit <= 0) {
                        return;
                    }
                }
            }

            BlockPos newPos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(2) - pRandom.nextInt(2), pRandom.nextInt(3) - 1);

            for(int k = 0; k < 4; k++) {
                if (pLevel.isEmptyBlock(newPos) && pState.canSurvive(pLevel, newPos)) {
                    pPos = newPos;
                }

                newPos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(2) - pRandom.nextInt(2), pRandom.nextInt(3) - 1);
            }

            if (pLevel.isEmptyBlock(newPos) && pState.canSurvive(pLevel, newPos)) {
                pLevel.setBlock(newPos, pState, 2);
            }
        }
    }

}