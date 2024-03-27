package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.Curse;
import com.favouriteless.enchanted.api.curses.CurseSavedData;
import com.favouriteless.enchanted.common.network.packets.EnchantedSinkingCursePacket;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.*;

public class CurseManager {

	public static final int MAX_LEVEL = 2;
	public static final Map<UUID, List<Curse>> ACTIVE_CURSES = new HashMap<>();

	public static void createCurse(ServerLevel level, CurseType<?> type, UUID targetUUID, UUID casterUUID, int curseLevel) {
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

	public static void tick(ServerLevel level) {
		if(level.dimension() == Level.OVERWORLD) {
			for(List<Curse> curses : ACTIVE_CURSES.values()) {
				for(Curse curse : curses) {
					curse.tick(level);
				}
			}
		}
	}

	public static void playerLoggedIn(ServerPlayer player) {
		CurseSavedData data = CurseSavedData.get(player.getLevel());
		UUID uuid = player.getUUID();
		if(data.entries.containsKey(uuid))
			ACTIVE_CURSES.put(uuid, data.entries.get(uuid));
		data.setDirty();
	}

	public static void playerLoggedOut(ServerPlayer player) { // Remove a player's curses when they log out
		ACTIVE_CURSES.remove(player.getUUID());
		Services.NETWORK.sendToPlayer(new EnchantedSinkingCursePacket(0.0D), player); // Remove any sinking curse values
	}

}
