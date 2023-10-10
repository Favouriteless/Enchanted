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
package com.favouriteless.enchanted.client.render.blockentity;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import com.favouriteless.enchanted.common.blockentities.SpinningWheelBlockEntity;
import com.favouriteless.enchanted.common.blocks.SpinningWheelBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerData;

public class SpinningWheelRenderer implements BlockEntityRenderer<SpinningWheelBlockEntity> {

	public static final ResourceLocation TEXTURE = Enchanted.location("textures/block/spinning_wheel.png");

	private final ModelPart wheel;
	private final ModelPart body;
	private final ModelPart frontArm;

	public SpinningWheelRenderer(Context context) {
		ModelPart root = context.bakeLayer(ModelLayerLocations.SPINNING_WHEEL);
		this.wheel = root.getChild("wheel");
		this.body = root.getChild("body");
		this.frontArm = root.getChild("frontArm");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("wheel", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, 2.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(-3.0F, -2.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(-3.0F, -3.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 6).addBox(2.0F, -3.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 28).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(18, 28).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(16, 31).addBox(-2.0F, -1.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(16, 30).addBox(0.0F, 0.0F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(5.1F, 14.2F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.396F, 18.7149F, 0.0F));
		Body.addOrReplaceChild("base2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.5F, -2.5F, 4.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4579F, -0.0037F, 0.0F, 0.0F, 0.0F, 0.2618F));
		Body.addOrReplaceChild("base1", CubeListBuilder.create().texOffs(0, 6).addBox(-2.5F, -0.5F, -1.5F, 6.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.3718F, -1.2978F, 0.0F, 0.0F, 0.0F, 0.2618F));
		Body.addOrReplaceChild("legS", CubeListBuilder.create().texOffs(28, 26).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(24, 26).addBox(-0.5F, -2.5F, -3.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9606F, 2.9883F, 1.5F, 0.0F, 0.0F, -0.1745F));
		Body.addOrReplaceChild("legE", CubeListBuilder.create().texOffs(20, 16).addBox(-0.5F, -3.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.9391F, 1.8151F, 0.0F, 0.0F, 0.0F, 0.1309F));
		Body.addOrReplaceChild("armS", CubeListBuilder.create().texOffs(24, 16).addBox(-0.5F, -4.8F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(28, 16).addBox(-0.5F, -4.8F, 1.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 0).addBox(-0.5F, -4.8F, 0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.354F, -0.7149F, -1.0F, 0.0F, 0.0F, 0.5672F));
		partdefinition.addOrReplaceChild("frontArm", CubeListBuilder.create().texOffs(20, 28).addBox(-0.5F, -2.75F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).texOffs(0, 27).addBox(-1.0F, -4.5F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.8123F, 16.6753F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void render(SpinningWheelBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();
		float rotationYDegrees = blockEntity.getLevel() != null ? blockEntity.getBlockState().getValue(SpinningWheelBlock.FACING).getOpposite().toYRot() : 0;

		poseStack.translate(0.5F, 1.5F, 0.5F);
		poseStack.mulPose(Vector3f.YN.rotationDegrees(rotationYDegrees));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));

		ContainerData furnaceData = blockEntity.getData();
		float cookTime = furnaceData.get(2) >= 1 ? furnaceData.get(2) + partialTicks - 1 : 0;
		float turnFactor = 25;
		float rotationDegreesWheel = cookTime % turnFactor * 360 / turnFactor;
		float rotationDegreesArm = rotationDegreesWheel * 2;

		VertexConsumer vertexBuilder = buffer.getBuffer((RenderType.entityTranslucentCull(TEXTURE)));
		body.render(poseStack, vertexBuilder, packedLight, packedOverlay);

		poseStack.pushPose();
		wheel.zRot = (float)(Math.PI + Math.toRadians(rotationDegreesWheel));
		wheel.render(poseStack, vertexBuilder, packedLight, packedOverlay);
		poseStack.popPose();

		poseStack.pushPose();
		frontArm.yRot = (float)(Math.PI + Math.toRadians(rotationDegreesArm));
		frontArm.render(poseStack, vertexBuilder, packedLight, packedOverlay);
		poseStack.popPose();

		poseStack.popPose();
	}
}