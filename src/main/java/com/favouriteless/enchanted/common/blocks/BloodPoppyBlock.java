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

package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.tileentity.BloodPoppyTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BloodPoppyBlock extends Block implements ITileEntityProvider {

    public static final BooleanProperty FILLED = BooleanProperty.create("filled");

    public BloodPoppyBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FILLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FILLED);
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!world.isClientSide && entity instanceof LivingEntity) {
            if(!state.getValue(FILLED)) {
                TileEntity tileEntity = world.getBlockEntity(pos);
                if(tileEntity instanceof BloodPoppyTileEntity) {
                    BloodPoppyTileEntity bloodPoppyTileEntity = (BloodPoppyTileEntity)tileEntity;
                    bloodPoppyTileEntity.setUUID(entity.getUUID());
                    bloodPoppyTileEntity.setName(entity.getDisplayName().getString());
                    world.setBlockAndUpdate(pos, state.setValue(FILLED, true));
                } else {
                    throw new IllegalStateException(String.format("Blood poppy at %s, %s, %s has invalid TileEntity", pos.getX(), pos.getY(), pos.getZ()));
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new BloodPoppyTileEntity();
    }

    public static void reset(World world, BlockPos pos) {
        if(!world.isClientSide) {
            BloodPoppyTileEntity tileEntity = (BloodPoppyTileEntity)world.getBlockEntity(pos);
            BlockState state = world.getBlockState(pos);

            world.setBlockAndUpdate(pos, state.setValue(FILLED, false));
            tileEntity.reset();
        }
    }
}
