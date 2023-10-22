package com.favouriteless.enchanted.api.curses;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

/**
 * {@link CurseSavedData} is where the {@link Curse}s on a {@link Player} are stored, by mapping player {@link UUID}s to
 * a {@link List} containing their {@link Curse}s
 */
public class CurseSavedData extends SavedData {

	private static final String NAME = "enchanted_curses";
	public final Map<UUID, List<Curse>> entries = new HashMap<>();

	public CurseSavedData() {
		super();
	}

	/**
	 * @param player The {@link Player} to grab data from.
	 *
	 * @return The {@link List} containing the {@link Curse}s the player has.
	 */
	public List<Curse> getEntry(Player player) {
		return getEntry(player.getUUID());
	}

	/**
	 * @param level The {@link Level} to access {@link MinecraftServer} from.
	 *
	 * @return An instance of {@link CurseSavedData} belonging to {@link Level#OVERWORLD} {@link Level}.
	 */
	public static CurseSavedData get(Level level) {
		if(level instanceof ServerLevel)
			return level.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(CurseSavedData::load, CurseSavedData::new, NAME);
		else
			throw new RuntimeException("Game attempted to load serverside curse data from a clientside world.");
	}

	// -------------------- IMPLEMENTATION  DETAILS BELOW THIS POINT, NOT NEEDED FOR API USERS --------------------

	private List<Curse> getEntry(UUID uuid) {
		return entries.computeIfAbsent(uuid, (_uuid) -> new ArrayList<>());
	}

	public static CurseSavedData load(CompoundTag nbt) {
		CurseSavedData data = new CurseSavedData();

		for(String key : nbt.getAllKeys()) {
			CompoundTag playerTag = (CompoundTag)nbt.get(key);
			List<Curse> curseList = new ArrayList<>();

			UUID uuid = playerTag.getUUID("uuid");
			ListTag listNBT = playerTag.getList("curses", Tag.TAG_COMPOUND);
			for(Tag tag : listNBT) {
				CompoundTag curseTag = (CompoundTag)tag;
				Curse curse = CurseTypes.getInstance(new ResourceLocation(curseTag.getString("type")));

				if(curse != null) {
					curse.load(curseTag);
					curseList.add(curse);
				}
				else
					Enchanted.LOG.info("Found saved Curse with invalid type, skipping.");
			}
			data.entries.put(uuid, curseList);
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		int i = 0;
		for(UUID uuid : entries.keySet()) {
			List<Curse> curses = this.entries.get(uuid);
			if(curses.isEmpty())
				break;

			CompoundTag saveTag = new CompoundTag();
			ListTag curseList = new ListTag();
			for(Curse curse : curses) {
				CompoundTag curseTag = new CompoundTag();
				curse.save(curseTag);
				curseList.add(curseTag);
			}
			saveTag.putUUID("uuid", uuid);
			saveTag.put("curses", curseList);
			nbt.put(String.valueOf(i++), saveTag);
		}
		return nbt;
	}

}
