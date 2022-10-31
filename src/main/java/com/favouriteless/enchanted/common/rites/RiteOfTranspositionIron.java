/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class RiteOfTranspositionIron extends AbstractCreateItemRite {

	private int progress = 0;
	public static final double CIRCLE_RADIUS = 7.5D;

	public RiteOfTranspositionIron() {
		super(0, 0, SoundEvents.COPPER_BREAK);
		CIRCLES_REQUIRED.put(CirclePart.LARGE, EnchantedBlocks.CHALK_PURPLE.get());
		ITEMS_REQUIRED.put(Items.ENDER_PEARL, 1);
		ITEMS_REQUIRED.put(Items.IRON_INGOT, 1);
		ITEMS_REQUIRED.put(Items.BLAZE_POWDER, 1);
		ITEMS_REQUIRED.put(EnchantedItems.DIAMOND_VAPOUR.get(), 1);
		ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected void onTick() {
		List<BlockPos> circlePoints = CirclePart.LARGE.getInsideCirclePoints();
		BlockPos testPos = circlePoints.get(progress++).offset(pos);

		for(int i = level.getMinBuildHeight(); i < pos.getY()-2; i++) {
			BlockPos blockPos = new BlockPos(testPos.getX(), i, testPos.getZ());
			Block block = level.getBlockState(blockPos).getBlock();
			if(block == Blocks.IRON_ORE) {
				level.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState());
				spawnItems(new ItemStack(Items.RAW_IRON));
				spawnMagicParticles();
			}
			else if(block == Blocks.DEEPSLATE_IRON_ORE) {
				level.setBlockAndUpdate(blockPos, Blocks.DEEPSLATE.defaultBlockState());
				spawnItems(new ItemStack(Items.RAW_IRON));
				spawnMagicParticles();
			}
		}

		if(this.ticks % 20 == 0) {
			level.sendParticles(EnchantedParticles.TRANSPOSITION_IRON_SEED.get(), pos.getX()+0.5D, pos.getY()-0.1D, pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
		}

		if(progress > circlePoints.size()-1)
			stopExecuting();
	}

	@Override
	protected CompoundTag saveAdditional(CompoundTag nbt) {
		nbt.putInt("progress", progress);
		return nbt;
	}

	@Override
	protected void loadAdditional(CompoundTag nbt) {
		if(nbt.contains("progress"))
			progress = nbt.getInt("progress");
	}

	@Override
	public RiteType<?> getType() {
		return EnchantedRiteTypes.TRANSPOSITION_IRON.get();
	}
}
