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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BroomstickRenderer extends EntityRenderer<BroomstickEntity> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/entity/broomstick.png");
	protected final BroomstickModel model = new BroomstickModel();

	public BroomstickRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(BroomstickEntity entity, float yaw, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int packedLight) {
		matrix.pushPose();

		matrix.translate(0.0D, 0.7D, 0.0D);
		matrix.mulPose(Vector3f.YP.rotationDegrees(entity.lerpRotY(partialTicks)));
		matrix.mulPose(Vector3f.XP.rotationDegrees(entity.lerpRotX(partialTicks)));

		this.model.setupAnim(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
		IVertexBuilder ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
		this.model.renderToBuffer(matrix, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrix.popPose();

		super.render(entity, yaw, partialTicks, matrix, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(BroomstickEntity pEntity) {
		return TEXTURE;
	}

}