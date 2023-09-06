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

package com.favouriteless.enchanted.common.init.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArthanaLootRegistry {

	private final Map<EntityType<?>, ItemStack> loot = new HashMap<>();

	public void register(EntityType<?> type, ItemStack drop) {
		loot.put(type, drop);
	}

	public ItemStack get(EntityType<?> entityType) {
		return loot.containsKey(entityType) ? loot.get(entityType).copy() : null;
	}

	public void reset() {
		loot.clear();
	}

}
