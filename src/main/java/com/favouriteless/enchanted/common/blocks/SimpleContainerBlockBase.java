/*
 *
 *   Copyright (c) 2023. Favouriteless
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

package com.favouriteless.enchanted.common.blocks;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public abstract class SimpleContainerBlockBase<T extends RandomizableContainerBlockEntity> extends BaseEntityBlock {

	public SimpleContainerBlockBase(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
		if(!worldIn.isClientSide) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if(tileEntity instanceof RandomizableContainerBlockEntity) {
				NetworkHooks.openGui((ServerPlayer) player, (RandomizableContainerBlockEntity)tileEntity, pos);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(blockPos);
			if (tileEntity instanceof RandomizableContainerBlockEntity) {
				Containers.dropContents(world, blockPos, (RandomizableContainerBlockEntity)tileEntity);
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}

}
