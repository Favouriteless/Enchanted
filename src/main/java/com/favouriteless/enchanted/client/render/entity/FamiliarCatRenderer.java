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
