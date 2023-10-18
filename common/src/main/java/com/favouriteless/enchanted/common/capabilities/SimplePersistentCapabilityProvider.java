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

package com.favouriteless.enchanted.common.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SimplePersistentCapabilityProvider<T extends INBTSerializable<CompoundTag>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	private final Capability<T> cap;
	private final T backend;
	private final LazyOptional<T> optionalData;

	public SimplePersistentCapabilityProvider(Capability<T> cap, T capImpl) {
		this.cap = cap;
		this.backend = capImpl;
		this.optionalData = LazyOptional.of(() -> backend);
	}

	@NotNull
	@Override
	public <C> LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
		return this.cap.orEmpty(cap, optionalData);
	}

	@Override
	public CompoundTag serializeNBT() {
		return backend.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		backend.deserializeNBT(nbt);
	}
}