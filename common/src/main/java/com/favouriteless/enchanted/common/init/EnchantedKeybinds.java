package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class EnchantedKeybinds {

	public static final String CATEGORY_BROOMSTICK = "key.enchanted.categories.broomstick";

	public static KeyMapping BROOM_FORWARD = new KeyMapping(getKeyName("broom_forward"), InputConstants.KEY_W, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_BACKWARD = new KeyMapping(getKeyName("broom_backward"), InputConstants.KEY_S, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_TURN_LEFT = new KeyMapping(getKeyName("broom_turn_left"), InputConstants.KEY_A, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_TURN_RIGHT = new KeyMapping(getKeyName("broom_turn_right"), InputConstants.KEY_D, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_UP = new KeyMapping(getKeyName("broom_up"), InputConstants.KEY_SPACE, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_DOWN = new KeyMapping(getKeyName("broom_down"), InputConstants.KEY_LCONTROL, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_AIM_UP = new KeyMapping(getKeyName("broom_aim_up"), InputConstants.KEY_C, CATEGORY_BROOMSTICK);
	public static KeyMapping BROOM_AIM_DOWN = new KeyMapping(getKeyName("broom_aim_down"), InputConstants.KEY_V, 	CATEGORY_BROOMSTICK);


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
