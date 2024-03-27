package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.RandomCurse;
import com.favouriteless.enchanted.common.init.EnchantedTags.Biomes;
import com.favouriteless.enchanted.common.init.registry.CurseTypes;
import net.minecraft.world.level.Level;

public class CurseOverheating extends RandomCurse {

	public CurseOverheating() {
		super(CurseTypes.OVERHEATING, 30, 90); // Executes once every 0.5-1.5 minutes
	}

	@Override
	protected void execute() {
		if(targetPlayer.getLevel().getBiome(targetPlayer.blockPosition()).is(Biomes.OVERHEATING_BIOMES)
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
