package net.favouriteless.enchanted.common.curses;

import net.favouriteless.enchanted.api.curses.RandomCurse;
import net.favouriteless.enchanted.common.init.ETags.Biomes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class CurseOverheating extends RandomCurse {

	public CurseOverheating() {
		super(CurseTypes.OVERHEATING, 30, 90); // Executes once every 0.5-1.5 minutes
	}

	@Override
	protected void execute(ServerPlayer target) {
		ServerLevel level = target.serverLevel();
		if(level.dimension() != Level.NETHER && !level.getBiome(target.blockPosition()).is(Biomes.OVERHEATING_BIOMES))
			return;

		int duration = 4;
		for(int i = 0; i < strength; i++) {
			if(Math.random() < 0.75D)
				duration += 4;
		}
		target.setRemainingFireTicks(duration * 20);
	}

}
