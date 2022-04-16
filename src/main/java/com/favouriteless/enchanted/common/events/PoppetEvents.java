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

package com.favouriteless.enchanted.common.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.items.poppets.PoppetUtils;
import com.favouriteless.enchanted.common.items.poppets.PoppetUtils.PoppetResult;
import com.favouriteless.enchanted.common.network.EnchantedPackets;
import com.favouriteless.enchanted.common.network.packets.EnchantedPoppetAnimationPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class PoppetEvents {

	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if(event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)event.getEntity();
			if(event.getAmount() >= player.getHealth()) { // Player would be killed by damage

				for(ItemStack poppetItem : player.inventory.items) {
					PoppetResult result = PoppetUtils.tryUseDeathPoppet(player, poppetItem, event.getSource());
					if(result == PoppetResult.SUCCESS || result == PoppetResult.SUCCESS_BREAK) {
						event.setCanceled(true);
					}
					if(result != PoppetResult.PASS) {
						if(!player.level.isClientSide) EnchantedPackets.sendToAllPlayers(new EnchantedPoppetAnimationPacket(result, poppetItem));
						return;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onItemBreak(PlayerDestroyItemEvent event) {
		ItemStack tool = event.getOriginal();
		for(ItemStack poppetItem : event.getPlayer().inventory.items) {
			PoppetResult result = PoppetUtils.tryUseToolPoppet(event.getPlayer(), poppetItem, tool);
			if(result == PoppetResult.SUCCESS || result == PoppetResult.SUCCESS_BREAK) {
				event.getPlayer().setItemInHand(event.getHand(), tool);
			}
			if(result != PoppetResult.PASS) {
				if(!event.getPlayer().level.isClientSide) EnchantedPackets.sendToAllPlayers(new EnchantedPoppetAnimationPacket(result, poppetItem));
				return;
			}
		}
	}

	public static void onLivingEntityBreak(LivingEntity entity, EquipmentSlotType slot) {
		if(entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			ItemStack armourItem = entity.getItemBySlot(slot).copy();
			if(armourItem.getItem() instanceof ArmorItem) {
				for(ItemStack poppetItem : player.inventory.items) {
					PoppetResult result = PoppetUtils.tryUseArmourPoppet(player, poppetItem, armourItem);
					if(result == PoppetResult.SUCCESS || result == PoppetResult.SUCCESS_BREAK) {
						player.setItemSlot(slot, armourItem);
					}
					if(result != PoppetResult.PASS) {
						if(!player.level.isClientSide) EnchantedPackets.sendToAllPlayers(new EnchantedPoppetAnimationPacket(result, poppetItem));
						return;
					}
				}
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