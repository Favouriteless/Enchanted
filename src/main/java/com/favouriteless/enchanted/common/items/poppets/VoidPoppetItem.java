/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.items.poppets;

import com.favouriteless.enchanted.common.init.PoppetColour;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class VoidPoppetItem extends AbstractDeathPoppetItem {

	public VoidPoppetItem(float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate) {
		super(failRate, durability, colour, sourcePredicate);
	}

	@Override
	public boolean canProtect(Player player) {
		return true;
	}

	@Override
	public void protect(Player player) {
		player.setHealth(1);
		Vec3 pos = player.position();
		player.teleportTo(pos.x, 257.0D, pos.z);
	}

}
