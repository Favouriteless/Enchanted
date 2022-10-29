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
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.List;

@EventBusSubscriber(modid= Enchanted.MOD_ID, bus= Bus.FORGE)
public class RiteManager {

	public static void addRite(AbstractRite rite) {
		if(rite.level != null) {
			RiteWorldSavedData data = RiteWorldSavedData.get(rite.level);
			data.ACTIVE_RITES.add(rite);
			data.setDirty();
		}
	}

	public static void removeRite(AbstractRite rite) {
		rite.isRemoved = true;
	}

	public static List<AbstractRite> getActiveRites(Level world) {
		RiteWorldSavedData data = RiteWorldSavedData.get(world);
		return data.ACTIVE_RITES;
	}

	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if(event.phase == Phase.START && event.world.dimension() == Level.OVERWORLD) {
			RiteWorldSavedData data = RiteWorldSavedData.get(event.world);

			data.ACTIVE_RITES.removeIf(rite -> rite.isRemoved);
			for(AbstractRite rite : data.ACTIVE_RITES) {
				rite.tick();
			}
			data.setDirty();
		}
	}

}
