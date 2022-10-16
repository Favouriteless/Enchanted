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

import com.favouriteless.enchanted.common.entities.MandrakeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class MandrakeModel extends EntityModel<MandrakeEntity> {

	private final ModelPart Body;
	private final ModelPart Head;
	private final ModelPart Legs;
	private final ModelPart LeftLeg;
	private final ModelPart leg_r1;
	private final ModelPart RightLeg;
	private final ModelPart leg_r2;
	private final ModelPart Arms;
	private final ModelPart LeftArm;
	private final ModelPart RightArm;
	private final ModelPart Leaves;
	private final ModelPart Face2_r1;

	public MandrakeModel() {
		texWidth = 32;
		texHeight = 32;

		Body = new ModelPart(this);
		Body.setPos(0.0F, 15.0F, 0.0F);
		Body.texOffs(0, 0).addBox(-3.0F, 1.0F, -3.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
		Body.texOffs(0, 10).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 6.0F, 5.0F, 0.0F, false);

		Head = new ModelPart(this);
		Head.setPos(0.0F, 1.0F, 0.0F);
		Body.addChild(Head);
		Head.texOffs(0, 25).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 3.0F, 4.0F, 0.0F, false);

		Legs = new ModelPart(this);
		Legs.setPos(0.0F, 0.0F, 0.0F);
		Body.addChild(Legs);
		

		LeftLeg = new ModelPart(this);
		LeftLeg.setPos(0.0F, 0.0F, 0.0F);
		Legs.addChild(LeftLeg);
		

		leg_r1 = new ModelPart(this);
		leg_r1.setPos(2.0F, 5.0F, 0.0F);
		LeftLeg.addChild(leg_r1);
		setRotationAngle(leg_r1, 0.0F, 0.0F, -0.1745F);
		leg_r1.texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		RightLeg = new ModelPart(this);
		RightLeg.setPos(0.0F, 0.0F, 0.0F);
		Legs.addChild(RightLeg);
		

		leg_r2 = new ModelPart(this);
		leg_r2.setPos(-2.0F, 5.0F, 0.0F);
		RightLeg.addChild(leg_r2);
		setRotationAngle(leg_r2, 0.0F, 0.0F, 0.1745F);
		leg_r2.texOffs(0, 0).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		Arms = new ModelPart(this);
		Arms.setPos(0.0F, 0.0F, 0.0F);
		Body.addChild(Arms);
		

		LeftArm = new ModelPart(this);
		LeftArm.setPos(0.0F, 0.0F, 0.0F);
		Arms.addChild(LeftArm);
		

		RightArm = new ModelPart(this);
		RightArm.setPos(0.0F, 0.0F, 0.0F);
		Arms.addChild(RightArm);
		

		Leaves = new ModelPart(this);
		Leaves.setPos(0.0F, 12.0F, 0.0F);
		setRotationAngle(Leaves, 0.0F, -2.3562F, 0.0F);
		Leaves.texOffs(16, 26).addBox(-4.0F, -6.0F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);

		Face2_r1 = new ModelPart(this);
		Face2_r1.setPos(0.0F, 0.0F, 0.0F);
		Leaves.addChild(Face2_r1);
		setRotationAngle(Face2_r1, 0.0F, -1.5708F, 0.0F);
		Face2_r1.texOffs(16, 26).addBox(-4.0F, -6.0F, 0.0F, 8.0F, 6.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(MandrakeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.leg_r1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.leg_r2.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Body.render(matrixStack, buffer, packedLight, packedOverlay);
		Leaves.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}