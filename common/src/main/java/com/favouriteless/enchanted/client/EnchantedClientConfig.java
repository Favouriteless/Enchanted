package com.favouriteless.enchanted.client;


public class EnchantedClientConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ConfigValue<Boolean> USE_ORIGINAL_CAT_TYPE;

	static {
		USE_ORIGINAL_CAT_TYPE = BUILDER.comment("Render cat familiars with their original fur colour rather than all black #default false").define("original_cat_type", false);
		SPEC = BUILDER.build();
	}

}