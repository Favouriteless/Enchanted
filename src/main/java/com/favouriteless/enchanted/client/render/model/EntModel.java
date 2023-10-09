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

package com.favouriteless.enchanted.client.render.model;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.entities.Ent;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class EntModel extends AnimatedTickingGeoModel<Ent> {

	private static final ResourceLocation MODEL = Enchanted.location("geo/ent.json");
	private static final ResourceLocation ANIMATION = Enchanted.location("animations/ent.json");

	@Override
	public ResourceLocation getAnimationFileLocation(Ent entity) {
		return ANIMATION;
	}

	@Override
	public ResourceLocation getModelLocation(Ent entity) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureLocation(Ent entity) {
		return Ent.TEXTURE_LOCATIONS[entity.getEntityData().get(Ent.TYPE)];
	}

	@Override
	public void setCustomAnimations(Ent animatable, int id, AnimationEvent event) {
		super.setCustomAnimations(animatable, id, event);
//		IBone head = this.getAnimationProcessor().getBone("head");
//
//		EntityModelData extraData = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
//		if (head != null) {
//			head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
//			head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
//		}
	}

}
