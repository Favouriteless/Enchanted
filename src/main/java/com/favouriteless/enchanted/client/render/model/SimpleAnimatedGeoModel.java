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
	public ResourceLocation getModelLocation(T object) {
		return modelLocation;
	}

	@Override
	public ResourceLocation getTextureLocation(T object) {
		return textureLocation;
	}

	@Override
	public ResourceLocation getAnimationFileLocation(T animatable) {
		return animationLocation;
	}
}
