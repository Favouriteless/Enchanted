package net.favouriteless.enchanted.client.client_handlers.entities;

import net.favouriteless.enchanted.common.entities.Broomstick;
import net.favouriteless.enchanted.common.init.EnchantedKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public class BroomstickEntityClientHandler {

	public static void controlBroom(Broomstick broom) {
		Options options = Minecraft.getInstance().options;

		broom.setInputs(
				options.keyUp.isDown(), options.keyDown.isDown(),
				options.keyLeft.isDown(), options.keyRight.isDown(),
				options.keyJump.isDown(), options.keySprint.isDown()
		);
	}


}
