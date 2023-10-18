package com.favouriteless.enchanted.client.render.model;

import com.favouriteless.enchanted.Enchanted;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SimpleAnimatedGeoModel<T extends IAnimatable> extends AnimatedGeoModel<T> {

	private final ResourceLocation modelLocation;
	private final ResourceLocation animationLocation;
	private final ResourceLocation textureLocation;

	public SimpleAnimatedGeoModel(String name) {
		this.modelLocation = Enchanted.location("geo/" + name + ".json");
		this.animationLocation = Enchanted.location("animations/" + name + ".json");;
		this.textureLocation = Enchanted.location("textures/entity/" + name + ".png");;
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
