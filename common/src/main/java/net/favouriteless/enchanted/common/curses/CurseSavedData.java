package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.Curse;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class CurseSavedData extends SavedData {

	private static final String NAME = Enchanted.savedDataName("curses");
	private final Map<UUID, List<Curse>> entries = new HashMap<>();

	public CurseSavedData() {
		super();
	}

	public static CurseSavedData get(ServerLevel level) {
		return level.getServer().overworld().getDataStorage().computeIfAbsent(new Factory<>(CurseSavedData::new, CurseSavedData::load, null), NAME);
	}

	public List<Curse> get(ServerPlayer player) {
		return get(player.getUUID());
	}

	public List<Curse> get(UUID uuid) {
		return entries.computeIfAbsent(uuid, k -> new ArrayList<>());
	}

	// -------------------- IMPLEMENTATION  DETAILS BELOW THIS POINT, NOT NEEDED FOR API USERS --------------------

	private static CurseSavedData load(CompoundTag nbt, Provider registries) {
		CurseSavedData data = new CurseSavedData();

		for(String key : nbt.getAllKeys()) {
			UUID target = UUID.fromString(key);
			List<Curse> curses = new ArrayList<>();

			for(Tag t : nbt.getList(key, Tag.TAG_COMPOUND)) {
				CompoundTag tag = (CompoundTag)t;
				Curse curse = CurseTypes.create(ResourceLocation.parse(tag.getString("type")));

				if(curse == null) {
					Enchanted.LOG.info("Found saved Curse with invalid type, skipping.");
					continue;
				}

				curse.load(tag);
				curses.add(curse);
			}
			data.entries.put(target, curses);
		}
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag nbt, Provider registries) {
		for(UUID uuid : entries.keySet()) {
			List<Curse> curses = entries.get(uuid);
			if(curses.isEmpty())
				continue;

			ListTag list = new ListTag();
			for(Curse curse : curses) {
				CompoundTag curseTag = new CompoundTag();
				curse.save(curseTag);
				list.add(curseTag);
			}

			nbt.put(uuid.toString(), list);
		}
		return nbt;
	}

}
