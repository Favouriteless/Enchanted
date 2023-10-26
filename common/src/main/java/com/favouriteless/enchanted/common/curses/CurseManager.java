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
import com.favouriteless.enchanted.api.curses.Curse;
import com.favouriteless.enchanted.api.curses.CurseSavedData;
import com.favouriteless.enchanted.common.network.packets.EnchantedSinkingCursePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.*;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class CurseManager {

	public static final int MAX_LEVEL = 2;
	public static final Map<UUID, List<Curse>> ACTIVE_CURSES = new HashMap<>();

	public static void createCurse(ServerLevel level, CurseType type, UUID targetUUID, UUID casterUUID, int curseLevel) {
		Curse curse = type.create();
		curse.setTarget(targetUUID);
		curse.setCaster(casterUUID);
		curse.setLevel(curseLevel);
		addCurse(level, curse);
	}

	public static void addCurse(ServerLevel level, Curse curse) {
		CurseSavedData data = CurseSavedData.get(level);
		UUID uuid = curse.getTargetUUID();
		if(data.entries.containsKey(uuid)) {
			List<Curse> curseList = data.entries.get(uuid);
			if(curseList.stream().noneMatch(oldCurse -> oldCurse.getType() == curse.getType())) { // If no equal curse
				curseList.add(curse);
			}
			else {
				for(int i = 0; i < curseList.size(); i++) {
					Curse oldCurse = curseList.get(i);
					if(oldCurse.getType() == curse.getType()) {
						if(oldCurse.getLevel() < curse.getLevel()) { // If new curse outlevels old matching curse
							curseList.set(i, curse);
							break;
						}
					}
				}
			}
		}
		else {
			List<Curse> newList = new ArrayList<>();
			newList.add(curse);
			data.entries.put(uuid, newList);
		}
		if(level.getServer().getPlayerList().getPlayer(uuid) != null) {
			ACTIVE_CURSES.put(uuid, data.entries.get(uuid));
		}
		data.setDirty();
	}

	public static void removeCurse(ServerLevel level, Curse curse) {
		CurseSavedData data = CurseSavedData.get(level);
		UUID uuid = curse.getTargetUUID();
		if(data.entries.containsKey(uuid)) {
			data.entries.get(uuid).remove(curse);
			curse.onRemove(level);
			data.setDirty();
		}
	}

	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if(event.phase == Phase.START && event.world.dimension() == Level.OVERWORLD && event.world instanceof ServerLevel level) {
			for(List<Curse> curses : ACTIVE_CURSES.values()) {
				for(Curse curse : curses) {
					curse.tick(level);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerLoggedInEvent event) { // Load a player's curses when they log in
		if(event.getPlayer() instanceof ServerPlayer player) {
			CurseSavedData data = CurseSavedData.get(player.getLevel());
			UUID uuid = player.getUUID();
			if(data.entries.containsKey(uuid))
				ACTIVE_CURSES.put(uuid, data.entries.get(uuid));
			data.setDirty();
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerLoggedOutEvent event) { // Remove a player's curses when they log out
		if(event.getPlayer() instanceof ServerPlayer player) {
			ACTIVE_CURSES.remove(player.getUUID());
			EnchantedPackets.sendToPlayer(new EnchantedSinkingCursePacket(0.0D), player); // Remove any sinking curse values
		}
	}

}
