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

package com.favouriteless.enchanted.common.items.poppets;

import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.Random;

public class PoppetUtils {

	public static final Random RANDOM = new Random();

	/**
	 * Check if a poppet is bound to an entity.
	 * @param item
	 * @return True if poppet has a UUID linked to it
	 */
	public static boolean isBound(ItemStack item) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			if(item.hasTag()) {
				return item.getTag().hasUUID("boundPlayer");
			}
		}
		return false;
	}

	/**
	 * Check if poppet belongs to a specified player
	 * @param item
	 * @param player
	 * @return True if poppet UUID matches player UUID
	 */
	public static boolean belongsTo(ItemStack item, PlayerEntity player) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			if(item.hasTag()) {
				CompoundNBT tag = item.getTag();
				if(tag.hasUUID("boundPlayer")) {
					return tag.getUUID("boundPlayer").equals(player.getUUID());
				}
			}
		}
		return false;
	}

	public static PlayerEntity getBoundPlayer(ItemStack item, World world) {
		if(isBound(item)) {
			return world.getPlayerByUUID(item.getTag().getUUID("boundPlayer"));
		}
		return null;
	}

	/**
	 * Bind poppet to a player.
	 * @param item
	 * @param player
	 */
	public static void bind(ItemStack item, PlayerEntity player) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			CompoundNBT tag = item.getOrCreateTag();
			tag.putUUID("boundPlayer", player.getUUID());
			item.setTag(tag);
		}
	}

	/**
	 * Unbind player from poppet
	 * @param item
	 */
	public static void unbind(ItemStack item) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			if(item.hasTag()) {
				CompoundNBT tag = item.getTag();
				tag.remove("boundPlayer");
				item.setTag(tag);
			}
		}
	}

	/**
	 * Attempts to consume a death protection poppet
	 * @param player
	 * @param item
	 * @param source
	 * @return Result of poppet use
	 */
	public static PoppetResult tryUseDeathPoppet(PlayerEntity player, ItemStack item, DamageSource source) {
		if(item.getItem() instanceof AbstractDeathPoppetItem) {
			AbstractDeathPoppetItem poppet = (AbstractDeathPoppetItem)item.getItem();
			if(PoppetUtils.belongsTo(item, player)) {
				if(poppet.protectsAgainst(source)) {
					if(poppet.canProtect(player)) {
						if(RANDOM.nextFloat() > poppet.getFailRate()) {
							poppet.protect(player);
							return tryDamagePoppet(item) ? PoppetResult.SUCCESS_BREAK : PoppetResult.SUCCESS;
						}
						return PoppetResult.FAIL;
					}
				}
			}
		}
		return PoppetResult.PASS;
	}

	/**
	 * Attempts to consume a death protection poppet
	 * @param player
	 * @param poppetStack
	 * @param toolStack
	 * @return Result of poppet use
	 */
	public static PoppetResult tryUseToolPoppet(PlayerEntity player, ItemStack poppetStack, ItemStack toolStack) {
		if(poppetStack.getItem() instanceof ToolPoppetItem) {
			ToolPoppetItem poppet = (ToolPoppetItem)poppetStack.getItem();
			if(PoppetUtils.belongsTo(poppetStack, player)) {
				if(toolStack.getItem().is(EnchantedTags.TOOLS)) {
					if(RANDOM.nextFloat() > poppet.getFailRate()) {
						poppet.protect(toolStack);
						return tryDamagePoppet(poppetStack) ? PoppetResult.SUCCESS_BREAK : PoppetResult.SUCCESS;
					}
					return PoppetResult.FAIL;
				}
			}
		}
		return PoppetResult.PASS;
	}

	/**
	 * Attempts to damage poppet
	 * @param item
	 * @return True if poppet is destroyed
	 */
	public static boolean tryDamagePoppet(ItemStack item) {
		item.setDamageValue(item.getDamageValue()+1);
		if(item.getDamageValue() >= item.getMaxDamage()) {
			item.shrink(1);
			return true;
		}
		return false;
	}

	public enum PoppetResult {
		SUCCESS,
		SUCCESS_BREAK,
		FAIL,
		PASS
	}
}
