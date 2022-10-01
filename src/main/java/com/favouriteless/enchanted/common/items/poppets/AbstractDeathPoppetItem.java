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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

import java.util.function.Predicate;

public abstract class AbstractDeathPoppetItem extends AbstractPoppetItem {

	protected final Predicate<DamageSource> sourcePredicate;


	public AbstractDeathPoppetItem(float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate) {
		super(failRate, durability, colour);
		this.sourcePredicate = sourcePredicate;
	}

	/**
	 * True if this poppet can protect against the specified DamageSource
	 * @param damageSource
	 * @return
	 */
	public boolean protectsAgainst(DamageSource damageSource) {
		return sourcePredicate.test(damageSource);
	}

	/**
	 * True if extra requirements are met (for example, random chance)
	 * @return
	 */
	public abstract boolean canProtect(PlayerEntity player);

	/**
	 * Apply protection effects to the given player
	 * @param player
	 */
	public abstract void protect(PlayerEntity player);

}
