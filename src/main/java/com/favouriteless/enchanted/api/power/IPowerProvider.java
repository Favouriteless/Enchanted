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

import com.favouriteless.enchanted.common.altar.AltarStateObserver;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.stateobserver.api.StateObserver;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Represents a {@link BlockEntity} which produces power. Every {@link BlockEntity} which needs to produce power should
 * implement this.
 *
 * <p>A {@link BlockEntity} implementing {@link IPowerProvider} should also use a {@link StateObserver} to
 * notify nearby {@link IPowerConsumer}s when it is created or loaded, and when those consumers are placed.</p>
 *
 * <p>See {@link AltarBlockEntity} and {@link AltarStateObserver} for an example implementation of a power provider.</p>
 */
public interface IPowerProvider {

    /**
     * Attempt to consume power from this {@link IPowerProvider}.
     *
     * @param amount The amount of power to be consumed.
     * @return True if the power was consumed, false if the power was not consumed (e.g. there was not enough power)
     */
    boolean tryConsumePower(double amount);

}
