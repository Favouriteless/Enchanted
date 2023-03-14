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

package com.favouriteless.enchanted.common.items.brews.throwable;

import com.favouriteless.enchanted.common.items.brews.ThrowableBrewItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LoveBrewItem extends ThrowableBrewItem {

	public static final double RANGE = 4.0D;

	public LoveBrewItem(Properties properties) {
		super(properties);
	}

	@Override
	public void applyEffect(Entity owner, Level level, Vec3 pos) {
		List<Animal> animals = level.getEntitiesOfClass(Animal.class, new AABB(pos.add(-RANGE, -RANGE, -RANGE), pos.add(RANGE, RANGE, RANGE)));
		for(Animal animal : animals) {
			Player source = owner instanceof Player ? (Player)owner : null;
			animal.setInLove(source);
		}
	}

	@Override
	public int getColour() {
		return 0xF78FEB;
	}

}
