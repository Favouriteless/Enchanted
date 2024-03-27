package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.api.rites.RiteSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class RiteManager {

	public static void addRite(AbstractRite rite) {
		if(rite.getLevel() != null) {
			RiteSavedData data = RiteSavedData.get(rite.getLevel());
			data.ACTIVE_RITES.add(rite);
			data.setDirty();
		}
	}

	public static void tick(ServerLevel level) {
		if(level.dimension() == Level.OVERWORLD) {
			RiteSavedData data = RiteSavedData.get(level);
			data.ACTIVE_RITES.removeIf(rite -> rite.isRemoved);
			data.setDirty();

			for(AbstractRite rite : data.ACTIVE_RITES)
				rite.tick();
		}
	}

}
