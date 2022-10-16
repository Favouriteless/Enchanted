/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.client.render.entity;


import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.model.BroomstickModel;
import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BroomstickRenderer extends EntityRenderer<BroomstickEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/entity/broomstick.png");
	protected final BroomstickModel model = new BroomstickModel();

	public BroomstickRenderer(EntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(BroomstickEntity entity, float yaw, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int packedLight) {
		matrix.pushPose();

		matrix.translate(0.0D, 0.7D, 0.0D);
		matrix.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
		matrix.mulPose(Vector3f.XP.rotationDegrees(180.0F - Mth.lerp(partialTicks, entity.xRotO, entity.xRot)));

		float f = entity.getHurtTime() - partialTicks;
		float f1 = entity.getDamage() - partialTicks;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}
		if (f > 0.0F) {
			matrix.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)entity.getHurtDir()));
		}

		this.model.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
		VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		this.model.renderToBuffer(matrix, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrix.popPose();

		super.render(entity, yaw, partialTicks, matrix, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(BroomstickEntity pEntity) {
		return TEXTURE;
	}

}