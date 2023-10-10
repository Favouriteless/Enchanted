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

package com.favouriteless.enchanted.common.poppet;

import com.favouriteless.enchanted.common.blockentities.PoppetShelfBlockEntity;
import com.favouriteless.enchanted.common.poppet.PoppetShelfSavedData.PoppetEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class PoppetShelfManager {

	public static PoppetShelfInventory getInventoryFor(PoppetShelfBlockEntity shelf) {
		if(shelf.getLevel() instanceof ServerLevel) {
			PoppetShelfSavedData data = PoppetShelfSavedData.get(shelf.getLevel());
			resolveAbsentShelf(data, shelf);
			return data.SHELF_STORAGE.get(PoppetShelfSavedData.getShelfIdentifier(shelf));
		}
		return null;
	}

	public static void removeShelf(PoppetShelfBlockEntity shelf) {
		if(shelf.getLevel() instanceof ServerLevel) {
			PoppetShelfSavedData data = PoppetShelfSavedData.get(shelf.getLevel());
			String identifier = PoppetShelfSavedData.getShelfIdentifier(shelf);
			data.removePoppetUUIDs(identifier, data.SHELF_STORAGE.get(identifier));
			data.SHELF_STORAGE.remove(identifier);
		}
	}

	public static void resolveAbsentShelf(PoppetShelfSavedData data, PoppetShelfBlockEntity shelf) {
		String identifier = PoppetShelfSavedData.getShelfIdentifier(shelf);
		if(!data.SHELF_STORAGE.containsKey(identifier)) {
			data.SHELF_STORAGE.put(identifier, new PoppetShelfInventory(shelf.getLevel(), shelf.getBlockPos()));
		}
	}

	public static List<PoppetEntry> getEntriesFor(Player player) {
		if(!player.level.isClientSide) {
			PoppetShelfSavedData data = PoppetShelfSavedData.get(player.level);
			if(data.PLAYER_POPPETS.containsKey(player.getUUID())) {
				List<PoppetEntry> list = data.PLAYER_POPPETS.get(player.getUUID());
				if(list != null)
					return list;
			}
		}
		return new ArrayList<>();
	}

}
