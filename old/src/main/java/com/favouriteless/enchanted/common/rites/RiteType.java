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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.UUID;

public class RiteType<T extends AbstractRite> extends ForgeRegistryEntry<RiteType<?>> {

	private final RiteFactory<T> factory;

	public RiteType(RiteFactory<T> factory) {
		this.factory = factory;
	}

	public T create(ServerLevel level, BlockPos pos, UUID caster) {
		return this.factory.create(this, level, pos, caster);
	}

	public T create() {
		return this.factory.create(this, null, null, null);
	}

	public interface RiteFactory<T extends AbstractRite> {
		T create(RiteType<T> type, ServerLevel level, BlockPos pos, UUID caster);
	}

}
