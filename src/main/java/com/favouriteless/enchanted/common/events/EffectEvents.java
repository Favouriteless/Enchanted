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

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class EffectEvents {

	@SubscribeEvent
	public static void effectsOnLivingEntityHurt(LivingHurtEvent event) {
		DamageSource source = event.getSource();
		LivingEntity entity = event.getEntityLiving();

		if(entity.hasEffect(EnchantedEffects.FALL_RESISTANCE.get())) {
			if(source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL) {
				event.setCanceled(true);
			}
		}
		if(entity.hasEffect(EnchantedEffects.DROWN_RESISTANCE.get())) {
			if(source == DamageSource.DROWN) {
				event.setCanceled(true);
			}
		}
	}

}
