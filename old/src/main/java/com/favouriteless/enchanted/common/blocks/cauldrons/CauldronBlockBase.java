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

package com.favouriteless.enchanted.common.blocks.cauldrons;

import com.favouriteless.enchanted.common.blockentities.CauldronBlockEntity;
import com.favouriteless.enchanted.core.util.PlayerInventoryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidAttributes;
import org.jetbrains.annotations.Nullable;

public abstract class CauldronBlockBase extends Block implements EntityBlock {

	public CauldronBlockBase(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity te = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if(te instanceof CauldronBlockEntity<?> cauldron) {
			if(cauldron.isComplete) {
				cauldron.takeContents(player);
				return InteractionResult.SUCCESS;
			}
			else if(stack.getItem() == Items.BUCKET && cauldron.isFailed) {
				cauldron.takeContents(player);
				world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
				return InteractionResult.SUCCESS;
			}
			else if(stack.getItem() == Items.BUCKET && cauldron.getWater() >= 1000) {
				if (!world.isClientSide) {
					if (cauldron.removeWater(FluidAttributes.BUCKET_VOLUME)) {
						world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
						stack.shrink(1);
						PlayerInventoryHelper.tryGiveItem(player, new ItemStack(Items.WATER_BUCKET));
					}
				}
				return InteractionResult.SUCCESS;
			}
			else if (stack.getItem() == Items.WATER_BUCKET) {
				if (!world.isClientSide) {
					if (cauldron.addWater(FluidAttributes.BUCKET_VOLUME)) {
						world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
						if (!player.isCreative()) player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
					}
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}

	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if(!world.isClientSide && entity instanceof ItemEntity) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof CauldronBlockEntity<?> cauldron) {
				if(!cauldron.isFailed && cauldron.isFull() && cauldron.isHot()) {
					cauldron.addItem((ItemEntity) entity);
				}
			}
		}
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return CauldronBlockEntity::tick;
	}
}
