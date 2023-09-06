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

package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.curses.AbstractCurse;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.*;

public class CurseSavedData extends SavedData {

	private static final String NAME = "enchanted_curses";
	public final Map<UUID, List<AbstractCurse>> curses = new HashMap<>();
	public final ServerLevel level;

	public CurseSavedData(ServerLevel level) {
		super();
		this.level = level;
	}

	public static CurseSavedData get(Level world) {
		if(world instanceof ServerLevel) {
			ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

			DimensionDataStorage storage = overworld.getDataStorage();
			return storage.computeIfAbsent((nbt) -> CurseSavedData.load(overworld, nbt), () -> new CurseSavedData(overworld), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load serverside curse data from a clientside world.");
		}
	}

	public static CurseSavedData load(ServerLevel level, CompoundTag nbt) {
		CurseSavedData data = new CurseSavedData(level);

		for(String key : nbt.getAllKeys()) {
			CompoundTag playerTag = (CompoundTag)nbt.get(key);
			List<AbstractCurse> curseList = new ArrayList<>();

			UUID uuid = playerTag.getUUID("uuid");
			ListTag listNBT = playerTag.getList("curses", 10);
			for(Tag tag : listNBT) {
				CompoundTag curseTag = (CompoundTag)tag;
				AbstractCurse curse = CurseTypes.getByName(new ResourceLocation(curseTag.getString("type")));

				if(curse != null) {
					curse.load(curseTag);
					curseList.add(curse);
				}
				else
					Enchanted.LOGGER.info("Curse with invalid type, skipping.");
			}
			data.curses.put(uuid, curseList);
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		int i = 0;
		for(UUID uuid : curses.keySet()) {
			List<AbstractCurse> curses = this.curses.get(uuid);
			if(curses.isEmpty())
				break;

			CompoundTag saveTag = new CompoundTag();
			ListTag curseList = new ListTag();
			for(AbstractCurse curse : curses) {
				CompoundTag curseTag = new CompoundTag();
				curse.save(curseTag);
				curseList.add(curseTag);
			}
			saveTag.putUUID("uuid", uuid);
			saveTag.put("curses", curseList);
			nbt.put(String.valueOf(i++), saveTag);
		}
		Enchanted.LOGGER.info("Saved curses successfully");
		return nbt;
	}
}
