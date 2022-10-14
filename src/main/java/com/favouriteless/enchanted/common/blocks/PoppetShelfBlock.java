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

package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.tileentity.PoppetShelfTileEntity;
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfManager;
import com.favouriteless.enchanted.core.util.StaticItemStackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class PoppetShelfBlock extends ContainerBlock {

	public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

	public PoppetShelfBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader reader) {
		return new PoppetShelfTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if(!worldIn.isClientSide) {
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof PoppetShelfTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (PoppetShelfTileEntity)tileEntity, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = world.getBlockEntity(blockPos);
			if(tileEntity instanceof PoppetShelfTileEntity) {
				PoppetShelfTileEntity shelf = (PoppetShelfTileEntity) tileEntity;
				if(!world.isClientSide)
					StaticItemStackHelper.dropContentsNoChange(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), shelf.getInventory());
				PoppetShelfManager.removeShelf(shelf);
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}
}
