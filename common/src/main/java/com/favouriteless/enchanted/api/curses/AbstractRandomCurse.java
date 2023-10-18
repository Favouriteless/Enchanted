/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.api.curses;

import com.favouriteless.enchanted.common.curses.CurseType;
import net.minecraft.nbt.CompoundTag;

public abstract class AbstractRandomCurse extends AbstractCurse {

	private final int minTime;
	private final int maxTime;
	private final double chance;
	private long lastUseTick = 0;

	public AbstractRandomCurse(CurseType type, int minTime, int maxTime) {
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
