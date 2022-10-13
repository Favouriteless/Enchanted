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

package com.favouriteless.enchanted.common.util;

import com.favouriteless.enchanted.common.items.poppets.AbstractPoppetItem;
import com.favouriteless.enchanted.common.tileentity.PoppetShelfTileEntity;
import com.favouriteless.enchanted.common.util.PoppetShelfWorldSavedData.PoppetEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PoppetShelfManager {

	public static void addPoppet(ItemStack itemStack, PoppetShelfTileEntity tileEntity) {
		if(tileEntity.getLevel() != null && tileEntity.getLevel() instanceof ServerWorld) {
			if(PoppetHelper.isBound(itemStack)) {
				UUID uuid = itemStack.getTag().getUUID("boundPlayer");
				PoppetEntry entry = new PoppetEntry((AbstractPoppetItem)itemStack.getItem(), uuid, (ServerWorld)tileEntity.getLevel(), tileEntity.getBlockPos());

				PoppetShelfWorldSavedData data = PoppetShelfWorldSavedData.get(tileEntity.getLevel());
				if(!data.PLAYER_POPPETS.containsKey(uuid))
					data.PLAYER_POPPETS.put(uuid, new ArrayList<>());
				data.PLAYER_POPPETS.get(uuid).add(entry);
				data.setDirty();
			}
		}
	}

	public static void removePoppet(ItemStack itemStack, PoppetShelfTileEntity tileEntity) {
		if(tileEntity.getLevel() != null && tileEntity.getLevel() instanceof ServerWorld) {
			if(PoppetHelper.isBound(itemStack)) {
				UUID uuid = itemStack.getTag().getUUID("boundPlayer");
				PoppetShelfWorldSavedData data = PoppetShelfWorldSavedData.get(tileEntity.getLevel());
				if(data.PLAYER_POPPETS.containsKey(uuid)) {
					List<PoppetEntry> entries = data.PLAYER_POPPETS.get(uuid);
					for(PoppetEntry entry : entries) {
						if(entry.matches(itemStack, tileEntity)) {
							entries.remove(entry);
							break;
						}
					}

					if(data.PLAYER_POPPETS.get(uuid).isEmpty())
						data.PLAYER_POPPETS.remove(uuid);
					data.setDirty();
				}
			}
		}
	}

	public static List<PoppetEntry> getEntriesFor(PlayerEntity player) {
		if(!player.level.isClientSide) {
			PoppetShelfWorldSavedData data = PoppetShelfWorldSavedData.get(player.level);
			if(data.PLAYER_POPPETS.containsKey(player.getUUID())) {
				List<PoppetEntry> list = data.PLAYER_POPPETS.get(player.getUUID());
				if(list != null)
					return list;
			}
		}
		return new ArrayList<>();
	}

}
