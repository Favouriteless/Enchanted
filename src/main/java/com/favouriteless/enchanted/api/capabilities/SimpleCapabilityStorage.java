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

package com.favouriteless.enchanted.api.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class SimpleCapabilityStorage<S extends INBT, C extends INBTSerializable<S>> implements Capability.IStorage<C> {
    private final Lazy<Capability<C>> capability;
    private final int expectedTagType;

    protected SimpleCapabilityStorage(final int expectedTagType, @Nonnull final Lazy<Capability<C>> capability) {
        this.expectedTagType = expectedTagType;
        this.capability = capability;
    }

    @Nonnull
    public static <S extends INBT, C extends INBTSerializable<S>> SimpleCapabilityStorage<S, C> create(@Nonnull final Supplier<Capability<C>> capability) {
        return create(capability, -1);
    }

    @Nonnull
    public static <S extends INBT, C extends INBTSerializable<S>> SimpleCapabilityStorage<S, C> create(@Nonnull final Supplier<Capability<C>> capability, final int expectedTagType) {
        return new SimpleCapabilityStorage<>(expectedTagType, Lazy.of(capability));
    }

    @Nullable
    @Override
    public INBT writeNBT(@Nonnull final Capability<C> capability, @Nonnull final C instance, @Nullable final Direction side) {
        if (capability != this.capability.get()) return null;
        return instance.serializeNBT();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readNBT(@Nonnull final Capability<C> capability, @Nonnull final C instance, @Nullable final Direction side, @Nonnull final INBT nbt) {
        if (capability != this.capability.get()) return;
        if (this.expectedTagType != -1 && nbt.getId() != this.expectedTagType) {
            throw new IllegalStateException("The NBT type " + nbt.getClass().getSimpleName() + " is not suitable for the capability " + capability);
        }
        instance.deserializeNBT((S) nbt);
    }
}