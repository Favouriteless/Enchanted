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

package com.favouriteless.enchanted.core.util;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StaticItemStackHelper {

	public static boolean canStack(ItemStack original, ItemStack other) {
		if(original.isStackable())
			if(original.sameItem(other))
				if(original.getCount() + other.getCount() <= original.getMaxStackSize())
					return original.getOrCreateTag().equals(other.getOrCreateTag());

		return false;
	}

	/**
	 * Similar to InventoryHelper#dropContents but works with copies of the itemstacks instead.
	 * @param level
	 * @param x
	 * @param y
	 * @param z
	 * @param inventory
	 */
	public static void dropContentsNoChange(Level level, double x, double y, double z, Container inventory) {
		for(int i = 0; i < inventory.getContainerSize(); ++i) {
			ItemStack item = inventory.getItem(i).copy();

			double d0 = EntityType.ITEM.getWidth();
			double d1 = 1.0D - d0;
			double d2 = d0 / 2.0D;
			double d3 = Math.floor(x) + Enchanted.RANDOM.nextDouble() * d1 + d2;
			double d4 = Math.floor(y) + Enchanted.RANDOM.nextDouble() * d1;
			double d5 = Math.floor(z) + Enchanted.RANDOM.nextDouble() * d1 + d2;

			while(!item.isEmpty()) {
				ItemEntity itementity = new ItemEntity(level, d3, d4, d5, item.split(Enchanted.RANDOM.nextInt(21) + 10));
				float f = 0.05F;
				itementity.setDeltaMovement(Enchanted.RANDOM.nextGaussian() * (double)0.05F, Enchanted.RANDOM.nextGaussian() * (double)0.05F + (double)0.2F, Enchanted.RANDOM.nextGaussian() * (double)0.05F);
				level.addFreshEntity(itementity);
			}
		}

	}

}
