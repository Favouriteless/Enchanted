package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.*;

public class CurseManagerImpl implements CurseManager {

	public static final CurseManagerImpl INSTANCE = new CurseManagerImpl();
	public static final int MAX_STRENGTH = 2;

	private final Map<UUID, List<Curse>> activeCurses = new HashMap<>();

	private CurseManagerImpl() {}

	@Override
	public void createCurse(CurseType<?> type, ServerLevel level, UUID target, int strength) {
		Curse curse = type.create();
		curse.setTargetUUID(target);
		curse.strength = strength;
		addCurse(level, curse);
	}

	@Override
	public List<Curse> getCursesFor(ServerPlayer player) {
		CurseSavedData data = CurseSavedData.get(player.serverLevel());
		return data.get(player);
	}

	@Override
	public void removeCurse(ServerLevel level, Curse curse) {
		CurseSavedData data = CurseSavedData.get(level);
		data.get(curse.getTargetUUID()).remove(curse);
		data.setDirty();
		curse.remove(level);
	}

	private void addCurse(ServerLevel level, Curse curse) {
		CurseSavedData data = CurseSavedData.get(level);
		UUID target = curse.getTargetUUID();

		List<Curse> curses = data.get(target);
		Optional<Curse> optional = curses.stream().filter(c -> c.type == curse.type).findFirst();

		if(optional.isPresent()) {
			Curse existing = optional.get();
			if(curse.strength < existing.strength)
				return;
			curses.remove(existing);
		}
		curses.add(curse);
		data.setDirty();

		if(level.getServer().getPlayerList().getPlayer(target) != null)
			activeCurses.put(target, curses);
	}

	public void tick(ServerLevel level) {
		if(level.dimension() != Level.OVERWORLD)
			return;

		for(List<Curse> curses : activeCurses.values()) {
			curses.forEach(c -> c.tick(level));
		}
	}

	public void playerLoggedIn(ServerPlayer player) {
		CurseSavedData data = CurseSavedData.get(player.serverLevel());
		UUID uuid = player.getUUID();
		activeCurses.put(uuid, data.get(uuid));
	}

	public void playerLoggedOut(ServerPlayer player) { // Remove a player's curses when they log out
		activeCurses.remove(player.getUUID());
	}

}
