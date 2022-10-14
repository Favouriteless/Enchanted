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

package com.favouriteless.enchanted.common.util.poppet;

import com.favouriteless.enchanted.common.tileentity.PoppetShelfTileEntity;
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfWorldSavedData.PoppetEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class PoppetShelfManager {

	public static PoppetShelfInventory getInventoryFor(PoppetShelfTileEntity shelf) {
		if(shelf.getLevel() instanceof ServerWorld) {
			PoppetShelfWorldSavedData data = PoppetShelfWorldSavedData.get(shelf.getLevel());
			resolveAbsentShelf(data, shelf);
			return data.SHELF_STORAGE.get(PoppetShelfWorldSavedData.getShelfIdentifier(shelf));
		}
		return null;
	}

	public static void removeShelf(PoppetShelfTileEntity shelf) {
		if(shelf.getLevel() instanceof ServerWorld) {
			PoppetShelfWorldSavedData data = PoppetShelfWorldSavedData.get(shelf.getLevel());
			String identifier = PoppetShelfWorldSavedData.getShelfIdentifier(shelf);
			data.removePoppetUUIDs(identifier, data.SHELF_STORAGE.get(identifier));
			data.SHELF_STORAGE.remove(identifier);
		}
	}

	public static void resolveAbsentShelf(PoppetShelfWorldSavedData data, PoppetShelfTileEntity shelf) {
		String identifier = PoppetShelfWorldSavedData.getShelfIdentifier(shelf);
		if(!data.SHELF_STORAGE.containsKey(identifier)) {
			data.SHELF_STORAGE.put(identifier, new PoppetShelfInventory(shelf.getLevel(), shelf.getBlockPos()));
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
