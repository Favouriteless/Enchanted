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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class EmberMossBlock extends BushBlock {

    public EmberMossBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        return !pLevel.isEmptyBlock(pPos.below()) && pLevel.getBlockState(pPos.below()).isFaceSturdy(pLevel, pPos.below(), Direction.UP);
    }
    
    @Override
    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.random.nextInt(6) == 0 && pLevel.isAreaLoaded(pPos, 4)) {
            BlockPos newPos = pPos.offset(new BlockPos(
                    RANDOM.nextInt(7) - 3,
                    RANDOM.nextInt(7) - 3,
                    RANDOM.nextInt(7) - 3));

            if(pLevel.isEmptyBlock(newPos) && this.canSurvive(pState, pLevel, pPos)) {
                pLevel.setBlockAndUpdate(newPos, pState);
            }
        }
    }

    @Override
    public void entityInside(BlockState pState, World pWorld, BlockPos pPos, Entity pEntity) {
        pEntity.setSecondsOnFire(1);
        super.entityInside(pState, pWorld, pPos, pEntity);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
    }
}