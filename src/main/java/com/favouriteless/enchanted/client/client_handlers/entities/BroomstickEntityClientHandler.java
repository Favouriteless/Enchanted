/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.client.client_handlers.entities;

import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public class BroomstickEntityClientHandler {

	public static void controlBroom(BroomstickEntity entity) {
		if(entity.isVehicle()) {
			LocalPlayer player = Minecraft.getInstance().player;
			entity.setInputJump(false);

			if(player.input.left) {
				entity.setDeltaRotY(entity.getDeltaRotY()-1);
			}
			if(player.input.right) {
				entity.setDeltaRotY(entity.getDeltaRotY()+1);
			}
			if(player.input.down) {
				entity.setDeltaRotX(entity.getDeltaRotX()-1);
			}
			if(player.input.up) {
				entity.setDeltaRotX(entity.getDeltaRotX()+1);
			}
			if(player.input.jumping) {
				entity.setInputJump(true);
			}

			entity.setYRot(entity.getYRot() + entity.getDeltaRotY());
			entity.setXRot(Mth.clamp(entity.getXRot() + entity.getDeltaRotX(), -30.0F, 30.0F));
			if(entity.hasPassenger(player)) {
				player.setYRot(player.getYRot() + entity.getDeltaRotY());
			}

			entity.setDeltaMovement(entity.getNewDeltaMovement());
		}
	}

}
