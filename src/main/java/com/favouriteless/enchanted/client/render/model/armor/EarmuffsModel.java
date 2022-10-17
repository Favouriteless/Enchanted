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

package com.favouriteless.enchanted.client.render.model.armor;

import com.favouriteless.enchanted.Enchanted;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EarmuffsModel extends HumanoidModel<LivingEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/models/armor/custom/earmuffs.png");

	public EarmuffsModel() {
		super(0.5f);
		texWidth = 32;
		texHeight = 16;

		head = new ModelPart(this);
		head.setPos(0.0F, 24.0F, 0.0F);
		head.texOffs(18, 2).addBox(4.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		head.texOffs(0, 2).addBox(4.5F, -5.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		head.texOffs(18, 2).addBox(-5.0F, -6.0F, -2.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		head.texOffs(0, 2).addBox(-5.5F, -5.5F, -1.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		head.texOffs(0, 13).addBox(-4.5F, -8.5F, -1.0F, 9.0F, 1.0F, 2.0F, 0.0F, false);
		head.texOffs(4, 9).addBox(3.5F, -7.5F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		head.texOffs(10, 9).addBox(-4.5F, -7.5F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);
	}



	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}