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

package com.favouriteless.enchanted.api.familiars;

import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.favouriteless.enchanted.common.familiars.types.CatFamiliarType;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import com.favouriteless.enchanted.common.rites.binding.RiteBindingFamiliar;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

/**
 * A {@link FamiliarType} defines which {@link EntityType} creates what type of familiar, as well as creating logic for
 * creating the new familiar {@link Entity} given an input of an {@link Entity} of the specified type. {@link FamiliarType}s
 * should be registered using a {@link DeferredRegister} to the registry in {@link FamiliarTypes}.
 *
 * <p>See {@link CatFamiliarType} for an example implementation of a {@link FamiliarType}</p>
 *
 * @param <T> The {@link Entity} class of the "input" entity (for example {@link Cat}.
 * @param <C> The {@link Entity} class of the "output" entity (for example {@link FamiliarCat}.
 */
public abstract class FamiliarType<T extends TamableAnimal, C extends TamableAnimal> extends ForgeRegistryEntry<FamiliarType<?, ?>> {

	private final Supplier<EntityType<T>> typeIn;
	private final Supplier<EntityType<C>> typeOut;

	public FamiliarType(Supplier<EntityType<T>> typeIn, Supplier<EntityType<C>> typeOut) {
		this.typeIn = typeIn;
		this.typeOut = typeOut;
	}

	/**
	 * @return The {@link EntityType} required for input entities of this {@link FamiliarType}.
	 */
	public EntityType<T> getTypeIn() {
		return typeIn.get();
	}

	/**
	 * @return The {@link EntityType} produced by this {@link FamiliarType}.
	 */
	public EntityType<C> getTypeOut() {
		return typeOut.get();
	}

	/**
	 * Creates a familiar {@link TamableAnimal} from the supplied {@link TamableAnimal}. Implementations do not need to
	 * change the owner, tamed state or name of the returned {@link TamableAnimal} as that is already handled by
	 * {@link RiteBindingFamiliar}.
	 */
	public abstract C create(T from);

	public C getFor(ServerLevel level) {
		return create(getTypeIn().create(level));
	}

}
