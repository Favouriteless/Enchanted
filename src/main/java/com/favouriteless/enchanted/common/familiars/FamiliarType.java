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

package com.favouriteless.enchanted.common.familiars;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public abstract class FamiliarType<T extends Entity, C extends TamableAnimal> extends ForgeRegistryEntry<FamiliarType<?, ?>> {

	private final Supplier<EntityType<T>> typeIn;
	private final Supplier<EntityType<C>> typeOut;

	public FamiliarType(Supplier<EntityType<T>> typeIn, Supplier<EntityType<C>> typeOut) {
		this.typeIn = typeIn;
		this.typeOut = typeOut;
	}

	public EntityType<T> getTypeIn() {
		return typeIn.get();
	}

	public EntityType<C> getTypeOut() {
		return typeOut.get();
	}

	/**
	 * Create a familiar entity from the supplied entity. Don't need to change setTamed or ownerUUID.
	 */
	public abstract C create(T from);

	public C getFor(ServerLevel level) {
		return create(getTypeIn().create(level));
	}

}
