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

package com.favouriteless.enchanted.client.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE, value=Dist.CLIENT)
public class ClientEvents {

	@SubscribeEvent
	public static void onRenderOverlayPost(RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.ALL) {
			PoppetAnimationManager.render(event.getMatrixStack(), event.getPartialTicks(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight());
		}
	}

	@SubscribeEvent
	public static void clientTick(ClientTickEvent event) {
		PoppetAnimationManager.tick();
	}

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		if(event.getItemStack().getItem() == Items.TOTEM_OF_UNDYING && EnchantedConfig.DISABLE_TOTEMS.get()) {
			event.getToolTip().add(new TextComponent("Totems are disabled (Enchanted config)").withStyle(ChatFormatting.RED));
		}
	}

	@SubscribeEvent
	public static void earmuffsPlaySound(PlaySoundEvent event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if(player != null) {
			if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EnchantedItems.EARMUFFS.get()) {
				if(event.getSound() instanceof AbstractSoundInstance sound) {
					sound.volume *= 0.03F;
					event.setSound(sound);
				}
			}
		}
	}

}
