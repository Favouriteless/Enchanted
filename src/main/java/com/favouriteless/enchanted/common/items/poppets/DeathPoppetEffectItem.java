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
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class DeathPoppetEffectItem extends AbstractDeathPoppetItem {

	protected Supplier<EffectInstance> deathEffect;

	public DeathPoppetEffectItem(float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate, Supplier<EffectInstance> deathEffect) {
		super(failRate, durability, colour, sourcePredicate);
		this.deathEffect = deathEffect;
	}

	@Override
	public boolean canProtect(PlayerEntity player) {
		return true;
	}

	@Override
	public void protect(PlayerEntity player) {
		player.setHealth(1.0F);
		player.addEffect(deathEffect.get());
	}
}
