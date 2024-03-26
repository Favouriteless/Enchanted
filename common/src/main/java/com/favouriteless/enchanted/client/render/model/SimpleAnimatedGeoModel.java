package com.favouriteless.enchanted.client.render.model;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SimpleAnimatedGeoModel<T extends IAnimatable> extends AnimatedGeoModel<T> {

	private final ResourceLocation modelLocation;
	private final ResourceLocation animationLocation;
	private final ResourceLocation textureLocation;

	public SimpleAnimatedGeoModel(String prefix, String name) {
		this.modelLocation = Enchanted.location(String.format("geo/%s/%s.geo.json", prefix, name));
		this.animationLocation = Enchanted.location(String.format("animations/%s/%s.animation.json", prefix, name));
		this.textureLocation = Enchanted.location(String.format("textures/%s/%s.png", prefix, name));
	}

	@Override
	public ResourceLocation getModelResource(T object) {
		return modelLocation;
	}

	@Override
	public ResourceLocation getTextureResource(T object) {
		return textureLocation;
	}

	@Override
	public ResourceLocation getAnimationResource(T animatable) {
		return animationLocation;
	}
}
