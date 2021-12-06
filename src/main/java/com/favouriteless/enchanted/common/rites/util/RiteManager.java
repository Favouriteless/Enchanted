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
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid= Enchanted.MOD_ID, bus= Bus.FORGE)
public class RiteManager {

	private static final List<AbstractRite> ACTIVE_RITES = new ArrayList<>();

	public static void addRite(AbstractRite rite) {
		ACTIVE_RITES.add(rite);
	}

	public static void removeRite(AbstractRite rite) {
		rite.isRemoved = true;
	}

	public static void clearRites() {
		ACTIVE_RITES.clear();
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent event) {
		if(event.phase == Phase.START) {
			ACTIVE_RITES.removeIf(abstractRite -> abstractRite.isRemoved);
			for(AbstractRite rite : ACTIVE_RITES) {
				rite.tick();
			}
		}
	}

}
