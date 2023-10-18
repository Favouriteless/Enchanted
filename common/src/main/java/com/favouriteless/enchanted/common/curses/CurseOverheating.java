package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.AbstractRandomCurse;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.world.level.Level;

public class CurseOverheating extends AbstractRandomCurse {

	public CurseOverheating() {
		super(CurseTypes.OVERHEATING, 30, 90); // Executes once every 0.5-1.5 minutes
	}

	@Override
	protected void execute() {
		if(ForgeRegistries.BIOMES.tags().getTag(EnchantedTags.Biomes.OVERHEATING_BIOMES).contains(targetPlayer.getLevel().getBiome(targetPlayer.blockPosition()).value())
		|| targetPlayer.getLevel().dimension() == Level.NETHER) {
			int duration = 4;
			for(int i = 0; i < level; i++) {
				if(Math.random() < 0.75D)
					duration += 4;
			}
			targetPlayer.setSecondsOnFire(duration);
		}
	}

}
