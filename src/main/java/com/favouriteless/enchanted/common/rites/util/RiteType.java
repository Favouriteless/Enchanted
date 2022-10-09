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

package com.favouriteless.enchanted.common.rites.util;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class RiteType<T extends AbstractRite> extends ForgeRegistryEntry<RiteType<?>> {

	private final Supplier<AbstractRite> supplier;

	public RiteType(Supplier<AbstractRite> supplier) {
		this.supplier = supplier;
	}

	public AbstractRite create() {
		return this.supplier.get();
	}

	public AbstractRite create(ServerWorld world) {
		AbstractRite rite = create();
		rite.setWorld(world);
		return rite;
	}

	public AbstractRite create(ServerWorld world, BlockPos pos) {
		AbstractRite rite = create();
		rite.setWorld(world);
		rite.setPos(pos);
		return rite;
	}

	public AbstractRite create(ServerWorld world, BlockPos pos, PlayerEntity caster) {
		AbstractRite rite = create();
		rite.setWorld(world);
		rite.setPos(pos);
		rite.setCaster(caster);
		return rite;
	}

}
