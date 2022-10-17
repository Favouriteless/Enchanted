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

package com.favouriteless.enchanted.common.util.rite;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class RiteWorldSavedData extends SavedData {

	private static final String NAME = "enchanted_rites";
	public final List<AbstractRite> ACTIVE_RITES = new ArrayList<>();
	public final ServerLevel world;
	
	public RiteWorldSavedData(ServerLevel world) {
		super(NAME);
		this.world = world;
	}

	public static RiteWorldSavedData get(Level world) {
		if (world instanceof ServerLevel) {
			ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

			DimensionDataStorage storage = overworld.getDataStorage();
			return storage.computeIfAbsent(() -> new RiteWorldSavedData(overworld), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load serverside rite data from a clientside world.");
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		ListTag riteList = nbt.getList("riteList", 10);

		for(int i = 0; i < riteList.size(); i++) {
			CompoundTag riteNbt = riteList.getCompound(i);
			RiteType<?> type = EnchantedRiteTypes.REGISTRY.get().getValue(new ResourceLocation(riteNbt.getString("type")));

			if(type != null) {
				AbstractRite rite = type.create();
				rite.load(riteNbt, this.world);
				ACTIVE_RITES.add(rite);
			}
			else {
				Enchanted.LOGGER.error("Invalid rite type found in world save. Rite will not be loaded.");
			}
		}
		Enchanted.LOGGER.info("Loaded active rites successfully");
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag riteList = new ListTag();

		for(AbstractRite rite : ACTIVE_RITES) {
			if(!rite.isRemoved && !rite.getStarting()) {
				riteList.add(rite.save());
			}
		}

		nbt.put("riteList", riteList);
		Enchanted.LOGGER.info("Saved active rites successfully");
		return nbt;
	}

}
