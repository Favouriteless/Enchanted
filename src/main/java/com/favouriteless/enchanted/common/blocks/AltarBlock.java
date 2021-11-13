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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.multiblock.altar.AltarMultiBlock;
import com.favouriteless.enchanted.common.multiblock.altar.AltarPartIndex;
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import com.favouriteless.enchanted.core.util.MultiBlockTools;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AltarBlock extends ContainerBlock {

    public static final EnumProperty<AltarPartIndex> FORMED = EnumProperty.create("formed", AltarPartIndex.class);
    public static final BooleanProperty FACING_X = BooleanProperty.create("facing_x");

    public AltarBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(FORMED, AltarPartIndex.UNFORMED).setValue(FACING_X, true));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!world.isClientSide()) {
            MultiBlockTools.formMultiblock(AltarMultiBlock.INSTANCE, world, pos);
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean pIsMoving) {
        if(!world.isClientSide()) {
            if(state != newState && state.getValue(FORMED) != AltarPartIndex.UNFORMED) {
                MultiBlockTools.breakMultiblock(AltarMultiBlock.INSTANCE, world, pos, state);
            }
        }
        super.onRemove(state, world, pos, newState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
        builder.add(FACING_X);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(state.getValue(FORMED) != AltarPartIndex.UNFORMED) {
            if (!world.isClientSide) {
                BlockPos cornerPos = AltarMultiBlock.INSTANCE.getBottomLowerLeft(world, pos, state);
                BlockState cornerState = world.getBlockState(cornerPos);

                if (cornerState.getValue(FORMED) == AltarPartIndex.P000) {
                    TileEntity tileEntity = world.getBlockEntity(cornerPos);
                    if (tileEntity instanceof AltarTileEntity) {
                        Enchanted.LOGGER.info((String.format("%d/%d", ((AltarTileEntity)tileEntity).currentPower, ((AltarTileEntity)tileEntity).maxPower)));
                    }
                }
                return ActionResultType.CONSUME;
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new AltarTileEntity();
    }



    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(FORMED) == AltarPartIndex.P000;
    }
}
