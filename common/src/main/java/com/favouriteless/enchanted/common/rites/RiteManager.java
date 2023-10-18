package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRite;
import net.minecraft.world.level.Level;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class RiteManager {

	public static void addRite(AbstractRite rite) {
		if(rite.getLevel() != null) {
			RiteSavedData data = RiteSavedData.get(rite.getLevel());
			data.ACTIVE_RITES.add(rite);
			data.setDirty();
		}
	}

	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		if(event.phase == Phase.START && !event.world.isClientSide && event.world.dimension() == Level.OVERWORLD) {
			RiteSavedData data = RiteSavedData.get(event.world);

			data.ACTIVE_RITES.removeIf(rite -> rite.isRemoved);
			for(AbstractRite rite : data.ACTIVE_RITES) {
				rite.tick();
			}
			data.setDirty();
		}
	}

}
