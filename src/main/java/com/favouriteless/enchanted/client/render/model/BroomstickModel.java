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

package com.favouriteless.enchanted.client.render.model;

import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BroomstickModel extends EntityModel<BroomstickEntity> {

	private final ModelPart bb_main;

	private final ModelPart Nose;
	private final ModelPart FrontSlope;
	private final ModelPart BackSlope;
	private final ModelPart Rear;

	private final ModelPart TailPlane2;
	private final ModelPart TailPlane1;
	private final ModelPart GoldBand;

	public BroomstickModel() {
		texWidth = 32;
		texHeight = 32;

		bb_main = new ModelPart(this);
		bb_main.setPos(0.0F, 0.0F, 0.0F);


		TailPlane2 = new ModelPart(this);
		TailPlane2.setPos(0.0F, 0.0F, -8.5F);
		bb_main.addChild(TailPlane2);
		setRotationAngle(TailPlane2, -3.1416F, 0.0F, -2.3562F);
		TailPlane2.texOffs(-12, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 12.0F, 0.0F, false);

		TailPlane1 = new ModelPart(this);
		TailPlane1.setPos(0.0F, 0.0F, -8.5F);
		bb_main.addChild(TailPlane1);
		setRotationAngle(TailPlane1, 3.1416F, 0.0F, 2.3562F);
		TailPlane1.texOffs(-12, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 12.0F, 0.0F, false);

		GoldBand = new ModelPart(this);
		GoldBand.setPos(0.0F, 0.0F, -11.0F);
		bb_main.addChild(GoldBand);
		setRotationAngle(GoldBand, -3.1416F, 0.0F, 3.1416F);
		GoldBand.texOffs(24, 0).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		FrontSlope = new ModelPart(this);
		FrontSlope.setPos(0.0F, 0.3F, 11.7F);
		bb_main.addChild(FrontSlope);
		setRotationAngle(FrontSlope, 2.7925F, 0.0F, 3.1416F);
		FrontSlope.texOffs(16, 16).addBox(-1.0F, -0.975F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

		BackSlope = new ModelPart(this);
		BackSlope.setPos(0.0F, 0.6F, 6.7F);
		bb_main.addChild(BackSlope);
		setRotationAngle(BackSlope, -2.9234F, 0.0F, -3.1416F);
		BackSlope.texOffs(0, 6).addBox(-1.0F, -0.975F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

		Nose = new ModelPart(this);
		Nose.setPos(0.0F, -0.75F, 13.1F);
		bb_main.addChild(Nose);
		setRotationAngle(Nose, -3.0543F, 0.0F, 3.1416F);
		Nose.texOffs(17, 25).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		Rear = new ModelPart(this);
		Rear.setPos(0.0F, 0.0F, -2.0F);
		bb_main.addChild(Rear);
		setRotationAngle(Rear, -3.1416F, 0.0F, 3.1416F);
		Rear.texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(BroomstickEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}