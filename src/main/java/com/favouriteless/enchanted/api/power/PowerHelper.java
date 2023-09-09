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

package com.favouriteless.enchanted.api.power;

import com.favouriteless.enchanted.api.power.IPowerConsumer.IPowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class PowerHelper {

	/**
	 * Attempt to grab a {@link com.favouriteless.enchanted.api.power.IPowerProvider} from an {@link IPowerPosHolder}.
	 * If one of the positions is for some reason invalid, the position will be removed.
	 *
	 * @param level The {@link Level} to look for altars in.
	 * @param holder The {@link IPowerPosHolder} containing the positions which need checking.
	 *
	 * @return The first valid {@link com.favouriteless.enchanted.api.power.IPowerProvider} found in level in the
	 * positions provided by holder.
	 */
	public static IPowerProvider tryGetPowerProvider(Level level, IPowerPosHolder holder) {
		List<BlockPos> providers = holder.getPositions();
		while(!providers.isEmpty()) {
			if(level != null) {
				BlockPos pos = providers.get(0);
				BlockEntity be = level.getBlockEntity(pos);
				if(be instanceof IPowerProvider provider)
					return provider;
				else
					providers.remove(pos);
			}
		}
		return null;
	}

}
