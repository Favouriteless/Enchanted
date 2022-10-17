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

package com.favouriteless.enchanted.client.render.entity;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.model.MandrakeModel;
import com.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import com.favouriteless.enchanted.common.entities.MandrakeEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MandrakeRenderer extends MobRenderer<MandrakeEntity, MandrakeModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/entity/mandrake.png");

    public MandrakeRenderer(Context context) {
        super(context, new MandrakeModel(context.bakeLayer(ModelLayerLocations.MANDRAKE)), 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(MandrakeEntity entity) {
        return TEXTURE;
    }
}
