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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class EnchantedKeybinds {

	public static final String CATEGORY_BROOMSTICK = "key.enchanted.categories.broomstick";

	public static KeyMapping BROOM_FORWARD = new KeyMapping(getKeyName("broom_forward"), InputConstants.KEY_W, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_BACKWARD = new KeyMapping(getKeyName("broom_backward"), InputConstants.KEY_S, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_TURN_LEFT = new KeyMapping(getKeyName("broom_turn_left"), InputConstants.KEY_A, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_TURN_RIGHT = new KeyMapping(getKeyName("broom_turn_right"), InputConstants.KEY_D, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_UP = new KeyMapping(getKeyName("broom_up"), InputConstants.KEY_SPACE, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_DOWN = new KeyMapping(getKeyName("broom_down"), InputConstants.KEY_LCONTROL, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_AIM_UP = new KeyMapping(getKeyName("broom_aim_up"), InputConstants.KEY_C, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_AIM_DOWN = new KeyMapping(getKeyName("broom_aim_down"), InputConstants.KEY_V, CATEGORY_BROOMSTICK);


	public static void registerKeybinds() {
		ClientRegistry.registerKeyBinding(BROOM_FORWARD);
		ClientRegistry.registerKeyBinding(BROOM_BACKWARD);
		ClientRegistry.registerKeyBinding(BROOM_TURN_LEFT);
		ClientRegistry.registerKeyBinding(BROOM_TURN_RIGHT);
		ClientRegistry.registerKeyBinding(BROOM_UP);
		ClientRegistry.registerKeyBinding(BROOM_DOWN);
		ClientRegistry.registerKeyBinding(BROOM_AIM_UP);
		ClientRegistry.registerKeyBinding(BROOM_AIM_DOWN);
	}

	private static String getKeyName(String name) {
		return "key." + Enchanted.MOD_ID + "." + name;
	}

}
