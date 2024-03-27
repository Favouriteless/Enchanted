package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.mixin.AbstractSoundInstanceAccessor;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ClientEvents {

	public static void onRenderGui(PoseStack poseStack, float partialTicks, Window window) {
		PoppetAnimationManager.render(poseStack, partialTicks, window.getGuiScaledWidth(), window.getGuiScaledHeight());
	}

	public static void clientTickPost() {
		PoppetAnimationManager.tick();
	}


	public static void onItemTooltip(ItemStack item, List<Component> toolTips, TooltipFlag flags) {
		if(item.getItem() == Items.TOTEM_OF_UNDYING && CommonConfig.DISABLE_TOTEMS.get())
			toolTips.add(Component.literal("Totems are disabled (Enchanted config)").withStyle(ChatFormatting.RED));
	}

	public static void playSound(SoundInstance soundInstance) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if(player != null) {
			if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EnchantedItems.EARMUFFS.get()) {
				if(soundInstance instanceof AbstractSoundInstance sound)
					((AbstractSoundInstanceAccessor)sound).setVolume(sound.getVolume() * 0.06F);
			}
		}
	}

}
