package com.favouriteless.enchanted.client.render.entity;

import com.favouriteless.enchanted.client.EnchantedClientConfig;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;

public class FamiliarCatRenderer extends CatRenderer {

	public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/cat/all_black.png");

	public FamiliarCatRenderer(Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Cat entity) {
		return EnchantedClientConfig.USE_ORIGINAL_CAT_TYPE.get() ? entity.getResourceLocation() : TEXTURE_LOCATION;
	}
	
}
