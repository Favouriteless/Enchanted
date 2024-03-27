package com.favouriteless.enchanted.api.curses;

import com.favouriteless.enchanted.common.curses.CurseType;
import net.minecraft.nbt.CompoundTag;

/**
 * An implementation of {@link Curse} which triggers randomly between a specified range of seconds.
 */
public abstract class RandomCurse extends Curse {

	private final int minTime;
	private final int maxTime;
	private final double chance;
	private long lastUseTick = 0;

	public RandomCurse(CurseType<?> type, int minTime, int maxTime) {
		super(type);
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.chance = 1.0D / ((maxTime - minTime)*20);
	}

	@Override
	protected void onTick() {
		long ticksSince = ticks - lastUseTick;
		if(ticksSince > maxTime*20L) {
			lastUseTick = ticks;
			execute();
		}
		else if(ticksSince > minTime*20L) {
			if(Math.random() < chance) {
				lastUseTick = ticks;
				execute();
			}
		}
	}

	protected abstract void execute();

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		nbt.putLong("lastUse", lastUseTick);
	}

	@Override
	protected void loadAdditional(CompoundTag nbt) {
		lastUseTick = nbt.getLong("lastUse");
	}
}
