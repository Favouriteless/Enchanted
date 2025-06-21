package net.favouriteless.enchanted.client.client_handlers.block_entities;

import net.favouriteless.enchanted.common.blocks.entity.CauldronBlockEntity;
import net.favouriteless.enchanted.client.CauldronBubblingSoundInstance;
import net.minecraft.client.Minecraft;

public class CauldronClientHandler {

	public static void startSound(CauldronBlockEntity<?> cauldron) {
		Minecraft.getInstance().getSoundManager().play(new CauldronBubblingSoundInstance(cauldron));
	}

}
