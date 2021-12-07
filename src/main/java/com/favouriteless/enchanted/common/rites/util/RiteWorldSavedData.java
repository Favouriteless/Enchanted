/*
 * Copyright (c) 2021. Favouriteless
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

package com.favouriteless.enchanted.common.rites.util;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;
import java.util.List;

public class RiteWorldSavedData extends WorldSavedData {

	private static final String NAME = "enchantedRites";
	public final List<AbstractRite> ACTIVE_RITES = new ArrayList<>();
	public final ServerWorld world;
	
	public RiteWorldSavedData(ServerWorld world) {
		super(NAME);
		this.world = world;
	}

	public static RiteWorldSavedData get(World world)
	{
		if (world instanceof ServerWorld) {
			ServerWorld overworld = world.getServer().getLevel(World.OVERWORLD);

			DimensionSavedDataManager storage = overworld.getDataStorage();
			return storage.computeIfAbsent(() -> new RiteWorldSavedData(overworld), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load serverside rite data from a clientside world.");
		}
	}

	@Override
	public void load(CompoundNBT nbt) {
		ListNBT riteList = nbt.getList("riteList", 10);

		for(int i = 0; i < riteList.size(); i++) {
			CompoundNBT riteNbt = riteList.getCompound(i);
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
	public CompoundNBT save(CompoundNBT nbt) {
		ListNBT riteList = new ListNBT();

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
