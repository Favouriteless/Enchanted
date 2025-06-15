package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import net.favouriteless.enchanted.common.CommonConfig;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.mixin.client.AbstractSoundInstanceAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

	public static void onRenderGui(GuiGraphics graphics, float partialTicks) {
		PoppetAnimationManager.render(graphics, partialTicks, graphics.guiWidth(), graphics.guiHeight());
	}

	public static void clientTickPost() {
		PoppetAnimationManager.tick();
	}


	public static void onItemTooltip(ItemStack item, List<Component> toolTips, TooltipFlag flags) {
		if(item.getItem() == Items.TOTEM_OF_UNDYING && CommonConfig.INSTANCE.disableTotems.get())
			toolTips.add(Component.literal("Totems are disabled (Enchanted config)").withStyle(ChatFormatting.RED));
	}

	public static void playSound(SoundInstance soundInstance) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if(player != null) {
			if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EItems.EARMUFFS.get()) {
				if(soundInstance instanceof AbstractSoundInstance sound) {
					AbstractSoundInstanceAccessor accessor = (AbstractSoundInstanceAccessor)sound;
					accessor.setVolume(accessor.getVolume() * 0.06F);
				}
			}
		}
	}

}
