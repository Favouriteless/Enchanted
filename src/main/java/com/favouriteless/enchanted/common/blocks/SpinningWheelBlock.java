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

import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.tileentity.DistilleryTileEntity;
import com.favouriteless.enchanted.common.tileentity.SpinningWheelTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class SpinningWheelBlock extends ContainerBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public SpinningWheelBlock(Properties properties) {
		super(properties);
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return EnchantedTileEntities.SPINNING_WHEEL.get().create();
	}

	@Nullable
	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new DistilleryTileEntity();
	}

	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		if(!worldIn.isClientSide) {
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof SpinningWheelTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (SpinningWheelTileEntity)tileEntity, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getBlockEntity(blockPos);
			if (tileentity instanceof SpinningWheelTileEntity) {
				SpinningWheelTileEntity tileEntity = (SpinningWheelTileEntity)tileentity;
				InventoryHelper.dropContents(world, blockPos, tileEntity);
			}
			super.onRemove(state, world, blockPos, newState, isMoving);  // call it last, because it removes the TileEntity
		}
	}


	//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}
}
