package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;

public class RiteSavedData extends SavedData {

	private static final String NAME = "enchanted_rites";
	public final List<AbstractRite> ACTIVE_RITES = new ArrayList<>();
	public final ServerLevel level;
	
	public RiteSavedData(ServerLevel world) {
		super();
		this.level = world;
	}

	public static RiteSavedData get(Level world) {
		if (world instanceof ServerLevel) {
			ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

			DimensionDataStorage storage = overworld.getDataStorage();
			return storage.computeIfAbsent((nbt) -> RiteSavedData.load(overworld, nbt), () -> new RiteSavedData(overworld), NAME);
		}
		else {
			throw new RuntimeException("Game attempted to load serverside rite data from a clientside world.");
		}
	}

	public static RiteSavedData load(ServerLevel level, CompoundTag nbt) {
		RiteSavedData data = new RiteSavedData(level);
		ListTag riteList = nbt.getList("riteList", 10);

		for(int i = 0; i < riteList.size(); i++) {
			CompoundTag riteNbt = riteList.getCompound(i);
			String typeString = riteNbt.getString("type");
			RiteType<?> type = RiteTypes.REGISTRY.get().getValue(new ResourceLocation(typeString));

			if(type != null) {
				AbstractRite rite = type.create();

				if(rite.load(riteNbt, data.level))
					data.ACTIVE_RITES.add(rite);
				else
					Enchanted.LOG.error("Failed to load rite of type" + typeString);
			}
			else
				Enchanted.LOG.error(String.format("Invalid rite type %s found in world save. Rite will not be loaded.", typeString));
		}
		Enchanted.LOG.info("Loaded active rites successfully");
		return data;
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		ListTag riteList = new ListTag();

		for(AbstractRite rite : ACTIVE_RITES) {
			if(!rite.isRemoved && !rite.isStarting()) {
				riteList.add(rite.save());
			}
		}

		nbt.put("riteList", riteList);
		return nbt;
	}

}
