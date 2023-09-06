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

package com.favouriteless.enchanted.common.util.poppet;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.items.poppets.AbstractDeathPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.AbstractPoppetItem;
import com.favouriteless.enchanted.common.items.poppets.ItemProtectionPoppetItem;
import com.favouriteless.enchanted.common.network.EnchantedPackets;
import com.favouriteless.enchanted.common.network.packets.EnchantedPoppetAnimationPacket;
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfSavedData.PoppetEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Queue;
import java.util.UUID;

public class PoppetHelper {

	public static boolean isBound(ItemStack item) {
		if(item.hasTag()) {
			return item.getTag().hasUUID("boundPlayer");
		}
		return false;
	}

	public static boolean belongsTo(ItemStack item, Player player) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			if(item.hasTag()) {
				CompoundTag tag = item.getTag();
				if(tag.hasUUID("boundPlayer")) {
					return tag.getUUID("boundPlayer").equals(player.getUUID());
				}
			}
		}
		return false;
	}

	public static boolean belongsTo(ItemStack item, UUID uuid) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			if(item.hasTag()) {
				CompoundTag tag = item.getTag();
				if(tag.hasUUID("boundPlayer")) {
					return tag.getUUID("boundPlayer").equals(uuid);
				}
			}
		}
		return false;
	}

	public static Player getBoundPlayer(ItemStack item, Level level) {
		if(isBound(item)) {
			return level.getServer().getPlayerList().getPlayer(item.getTag().getUUID("boundPlayer"));
		}
		return null;
	}

	public static String getBoundName(ItemStack item) {
		if(isBound(item)) {
			return item.getTag().getString("boundName");
		}
		return "None";
	}

	public static void bind(ItemStack item, Player player) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			CompoundTag tag = item.getOrCreateTag();
			tag.putUUID("boundPlayer", player.getUUID());
			tag.putString("boundName", player.getDisplayName().getString());
			item.setTag(tag);
		}
	}

	public static void unbind(ItemStack item) {
		if(item.getItem() instanceof AbstractPoppetItem) {
			if(item.hasTag()) {
				CompoundTag tag = item.getTag();
				tag.remove("boundPlayer");
				tag.remove("boundName");
				item.setTag(tag);
			}
		}
	}

	public static PoppetResult tryUseDeathPoppet(Player player, ItemStack poppetStack, ServerLevel level, String shelfIdentifier) {
		if(poppetStack.getItem() instanceof AbstractDeathPoppetItem poppet) {
			if(PoppetHelper.belongsTo(poppetStack, player)) {
				if(poppet.canProtect(player)) {
					if(Enchanted.RANDOM.nextFloat() > poppet.getFailRate()) {
						poppet.protect(player);
						level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.5F, 1.0F);
						return tryDamagePoppet(poppetStack, level, shelfIdentifier) ? PoppetResult.SUCCESS_BREAK : PoppetResult.SUCCESS;
					}
					return PoppetResult.FAIL;
				}
			}
		}
		return PoppetResult.PASS;
	}

	public static boolean tryUseDeathPoppetQueue(Queue<ItemStack> queue, Player player) {
		while(!queue.isEmpty()) {
			ItemStack poppetItem = queue.remove();
			if(handleTryUseDeathPoppet(player, poppetItem, null))
				return true;
		}
		return false;
	}

	public static boolean tryUseDeathPoppetEntryQueue(Queue<PoppetEntry> queue, Player player) {
		while(!queue.isEmpty()) {
			PoppetEntry entry = queue.remove();
			if(handleTryUseDeathPoppet(player, entry.item(), entry.shelfIdentifier()))
				return true;
		}
		return false;
	}

	public static boolean handleTryUseDeathPoppet(Player player, ItemStack item, String shelfIdentifier) {
		ItemStack poppetItemOriginal = item.copy();
		PoppetResult result = PoppetHelper.tryUseDeathPoppet(player, item, (ServerLevel)player.level, shelfIdentifier);
		return handlePoppetResult(result, poppetItemOriginal, player);
	}

	public static PoppetResult tryUseItemProtectionPoppet(Player player, ItemStack poppetStack, ItemStack toolStack, ServerLevel level, String shelfIdentifier) {
		if(poppetStack.getItem() instanceof ItemProtectionPoppetItem poppet) {
			if(PoppetHelper.belongsTo(poppetStack, player)) {
				if(Enchanted.RANDOM.nextFloat() > poppet.getFailRate()) {
					poppet.protect(toolStack);
					return tryDamagePoppet(poppetStack, level, shelfIdentifier) ? PoppetResult.SUCCESS_BREAK : PoppetResult.SUCCESS;
				}
				return PoppetResult.FAIL;
			}
		}
		return PoppetResult.PASS;
	}

	public static boolean tryUseItemProtectionPoppetQueue(Queue<ItemStack> queue, Player player, ItemStack toolStack) {
		while(!queue.isEmpty()) {
			ItemStack poppetItem = queue.remove();
			if(handleTryUseItemProtectionPoppet(player, poppetItem, toolStack, null))
				return true;
		}
		return false;
	}

	public static boolean tryUseItemProtectionPoppetEntryQueue(Queue<PoppetEntry> queue, Player player, ItemStack toolStack) {
		while(!queue.isEmpty()) {
			PoppetEntry entry = queue.remove();
			if(handleTryUseItemProtectionPoppet(player, entry.item(), toolStack, entry.shelfIdentifier()))
				return true;
		}
		return false;
	}

	public static boolean handleTryUseItemProtectionPoppet(Player player, ItemStack poppetStack, ItemStack toolStack, String shelfIdentifier) {
		ItemStack poppetItemOriginal = poppetStack.copy();
		PoppetResult result = PoppetHelper.tryUseItemProtectionPoppet(player, poppetStack, toolStack, (ServerLevel)player.level, shelfIdentifier);
		return handlePoppetResult(result, poppetItemOriginal, player);
	}


	/**
	 * Attempts to damage poppet
	 * @param item
	 * @return True if poppet is destroyed
	 */
	public static boolean tryDamagePoppet(ItemStack item, ServerLevel level, String shelfIdentifier) {
		item.setDamageValue(item.getDamageValue()+1);
		if(item.getDamageValue() >= item.getMaxDamage()) {
			item.shrink(1);
			if(shelfIdentifier != null && item.getCount() <= 0) {
				PoppetShelfSavedData data = PoppetShelfSavedData.get(level);
				PoppetShelfInventory inventory = data.SHELF_STORAGE.get(shelfIdentifier);
				for(int i = 0; i < inventory.getContainerSize(); i++)
					if(inventory.get(i).equals(item))
						inventory.set(i, ItemStack.EMPTY);
				data.updateShelf(shelfIdentifier);
			}
			return true;
		}
		return false;
	}

	private static boolean handlePoppetResult(PoppetResult result, ItemStack poppetItemOriginal, Player player) {
		if(result == PoppetResult.SUCCESS || result == PoppetResult.SUCCESS_BREAK) {
			if(!player.level.isClientSide)
				EnchantedPackets.sendToAllPlayers(new EnchantedPoppetAnimationPacket(result, poppetItemOriginal, player.getId()));
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
