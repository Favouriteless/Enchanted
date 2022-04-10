/*
 * Copyright (c) 2021. Favouriteless
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

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.init.EnchantedEffects;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class CommonEvents {

	@SubscribeEvent
	public static void effectsOnLivingEntityHurt(LivingHurtEvent event) {
		if(event.getSource() == DamageSource.FALL) {
			if(event.getEntityLiving().getEffect(EnchantedEffects.FALL_RESISTANCE.get()) != null) {
				event.setCanceled(true);
			}
		}
		else if(event.getSource() == DamageSource.DROWN) {
			if(event.getEntityLiving().getEffect(EnchantedEffects.DROWN_RESISTANCE.get()) != null) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		if(event.getItemStack().getItem() == Items.TOTEM_OF_UNDYING && EnchantedConfig.DISABLE_TOTEMS.get()) {
			event.getToolTip().add(new StringTextComponent("Totems are disabled (Enchanted config)").withStyle(TextFormatting.RED));
		}
	}

}
