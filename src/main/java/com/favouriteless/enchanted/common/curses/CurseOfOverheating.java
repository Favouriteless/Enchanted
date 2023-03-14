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

package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.AbstractRandomCurse;
import com.favouriteless.enchanted.common.init.EnchantedCurseTypes;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraftforge.registries.ForgeRegistries;

public class CurseOfOverheating extends AbstractRandomCurse {

	public CurseOfOverheating() {
		super(EnchantedCurseTypes.OVERHEATING.get(), 30, 90); // Executes once every 0.5-1.5 minutes
	}

	@Override
	protected void execute() {
		if(ForgeRegistries.BIOMES.tags().getTag(EnchantedTags.Biomes.OVERHEATING_BIOMES).contains(targetPlayer.getLevel().getBiome(targetPlayer.blockPosition()).value())) {
			int duration = 4;
			for(int i = 0; i < level; i++) {
				if(Math.random() < 0.75D)
					duration += 4;
			}
			targetPlayer.setSecondsOnFire(duration);
		}
	}

}
