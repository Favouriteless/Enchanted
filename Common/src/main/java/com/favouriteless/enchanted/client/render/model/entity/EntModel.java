package com.favouriteless.enchanted.client.render.model.entity;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.entities.Ent;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class EntModel extends AnimatedTickingGeoModel<Ent> {

	private static final ResourceLocation MODEL = Enchanted.location("geo/entity/ent.geo.json");
	private static final ResourceLocation ANIMATION = Enchanted.location("animations/entity/ent.animation.json");

	@Override
	public ResourceLocation getAnimationResource(Ent entity) {
		return ANIMATION;
	}

	@Override
	public ResourceLocation getModelResource(Ent entity) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(Ent entity) {
		return Ent.TEXTURE_LOCATIONS[entity.getEntityData().get(Ent.TYPE)];
	}

	@Override
	public void setCustomAnimations(Ent ent, int id, AnimationEvent event) {
		super.setCustomAnimations(ent, id, event);
//		IBone head = this.getAnimationProcessor().getBone("head");
//
//		EntityModelData extraData = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
//		if (head != null) {
//			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
//			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
//		}
	}

}
