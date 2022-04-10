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

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Random;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class PoppetUtils {

	public static final Random RANDOM = new Random();

	/**
	 * Returns true if the poppet is bound to a player.
	 * @param item
	 * @return
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
	 * Returns true if the poppet is bound to the specified player
	 * @param item
	 * @param player
	 * @return
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
	 * Attempts to consume a poppet
	 * @param player
	 * @param item
	 * @param source
	 * @return
	 */
	public static PoppetResult tryUsePoppet(PlayerEntity player, ItemStack item, DamageSource source) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			AbstractPoppetItem poppet = (AbstractPoppetItem)item.getItem();
			if(PoppetUtils.belongsTo(item, player)) {
				if(poppet.protectsAgainst(source)) {
					if(poppet.canProtect(player)) {
						if(RANDOM.nextFloat() > poppet.getFailRate()) {
							poppet.protect(player);
							return poppet.getDamage(item) > 0 ? PoppetResult.SUCCESS : PoppetResult.SUCCESS_BREAK;
						}
						return PoppetResult.FAIL;
					}
				}
			}
		}
		return PoppetResult.PASS;
	}

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			if(event.getAmount() >= player.getHealth()) { // Player would be killed by damage

				for(ItemStack item : player.inventory.items) {
					PoppetResult result = tryUsePoppet(player, item, event.getSource());
					if(result == PoppetResult.SUCCESS || result == PoppetResult.SUCCESS_BREAK) {
						event.setCanceled(true);
						break;
					}
				}
			}
		}
	}

	public enum PoppetResult {
		SUCCESS,
		SUCCESS_BREAK,
		FAIL,
		PASS
	}
}
