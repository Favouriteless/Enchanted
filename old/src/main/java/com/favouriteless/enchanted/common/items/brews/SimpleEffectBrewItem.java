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

package com.favouriteless.enchanted.common.items.brews;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class SimpleEffectBrewItem extends ConsumableBrewItem {

	private final MobEffect effect;
	private final int duration;
	private final int amplifier;

	public SimpleEffectBrewItem(MobEffect effect, int duration, int amplifier, Properties properties) {
		super(properties);
		this.effect = effect;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
		stack.shrink(1);
		entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
		return super.finishUsingItem(stack, world, entity);
	}
}
