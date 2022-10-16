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

package com.favouriteless.enchanted.common.effects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import javax.annotation.Nullable;

/**
 * This class should not have to exist in any way or for any reason, I just had to add it to fix mojang's janky ass Effect class with a protected constructor and giant else if chains.
 */
public class SimpleEffect extends MobEffect {

	public SimpleEffect(MobEffectCategory effectType, int color) {
		super(effectType, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entity, int amplifier, double health) {

	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return false;
	}

}
