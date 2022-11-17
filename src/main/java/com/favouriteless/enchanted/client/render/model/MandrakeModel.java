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

package com.favouriteless.enchanted.client.render.model;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.entities.MandrakeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MandrakeModel extends AnimatedGeoModel<MandrakeEntity> {

	public static final ResourceLocation MODEL_LOCATION = Enchanted.location("geo/mandrake.json");
	public static final ResourceLocation ANIMATION_LOCATION = Enchanted.location("animations/mandrake.json");
	public static final ResourceLocation TEXTURE_LOCATION = Enchanted.location("textures/entity/mandrake.png");

	@Override
	public ResourceLocation getModelLocation(MandrakeEntity object) {
		return MODEL_LOCATION;
	}

	@Override
	public ResourceLocation getTextureLocation(MandrakeEntity object) {
		return TEXTURE_LOCATION;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(MandrakeEntity animatable) {
		return ANIMATION_LOCATION;
	}

}
