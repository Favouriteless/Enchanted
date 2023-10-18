package com.favouriteless.enchanted.client.client_handlers.entities;

import com.favouriteless.enchanted.common.entities.Broomstick;
import com.favouriteless.enchanted.common.init.EnchantedKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public class BroomstickEntityClientHandler {

	public static void controlBroom(Broomstick broom) {
		if(broom.isVehicle()) {
			LocalPlayer player = Minecraft.getInstance().player;

			broom.setInputAcceleration(EnchantedKeybinds.BROOM_FORWARD.isDown(), EnchantedKeybinds.BROOM_BACKWARD.isDown());
			broom.setInputClimb(EnchantedKeybinds.BROOM_UP.isDown(), EnchantedKeybinds.BROOM_DOWN.isDown());

			if(EnchantedKeybinds.BROOM_TURN_LEFT.isDown())
				broom.setDeltaRotY(broom.getDeltaRotY()-1);
			if(EnchantedKeybinds.BROOM_TURN_RIGHT.isDown())
				broom.setDeltaRotY(broom.getDeltaRotY()+1);

			boolean flag = false;
			if(EnchantedKeybinds.BROOM_AIM_DOWN.isDown()) {
				broom.setDeltaRotX(broom.getDeltaRotX() + 1);
				flag = true;
			}
			if(EnchantedKeybinds.BROOM_AIM_UP.isDown()) {
				broom.setDeltaRotX(broom.getDeltaRotX() - 1);
				flag = true;
			}

			if(!flag) {
				if(Math.abs(0 - broom.getXRot()) < Math.abs(broom.getDeltaRotX()))
					broom.setDeltaRotX(-broom.getXRot());
				else if(broom.getXRot() > 0)
					broom.setDeltaRotX(broom.getDeltaRotX() - 1);
				else if(broom.getXRot() < 0)
					broom.setDeltaRotX(broom.getDeltaRotX() + 1);
			}

			broom.setYRot(broom.getYRot() + broom.getDeltaRotY());
			broom.setXRot(Mth.clamp(broom.getXRot() + broom.getDeltaRotX(), -30.0F, 30.0F));
			if(broom.hasPassenger(player)) {
				player.setYRot(player.getYRot() + broom.getDeltaRotY());
			}

			broom.setDeltaMovement(broom.getNewDeltaMovement());
		}
	}

}
