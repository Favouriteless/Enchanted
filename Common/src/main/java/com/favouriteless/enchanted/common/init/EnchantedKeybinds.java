package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.platform.Services;
import com.favouriteless.enchanted.platform.services.IClientRegistryHelper.KeyConflictContext;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class EnchantedKeybinds {

	public static final String CATEGORY_BROOMSTICK = "key.enchanted.categories.broomstick";

	public static final KeyMapping BROOM_AIM_UP = Services.CLIENT_REGISTRY.register(getKeyName("broom_aim_up"), InputConstants.KEY_C, CATEGORY_BROOMSTICK, KeyConflictContext.IN_GAME);
	public static final KeyMapping BROOM_AIM_DOWN = Services.CLIENT_REGISTRY.register(getKeyName("broom_aim_down"), InputConstants.KEY_V, CATEGORY_BROOMSTICK, KeyConflictContext.IN_GAME);



	private static String getKeyName(String name) {
		return "key." + Enchanted.MOD_ID + "." + name;
	}

	public static void load() {}

}
