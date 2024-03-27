package com.favouriteless.enchanted.client.render.model.entity;

import com.favouriteless.enchanted.common.entities.Broomstick;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BroomstickModel extends EntityModel<Broomstick> {
	private final ModelPart root;

	public BroomstickModel(ModelPart root) {
		this.root = root.getChild("root");
	}

	public static LayerDefinition createLayerDefinition() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		root.addOrReplaceChild("TailPlane2", CubeListBuilder.create().texOffs(-12, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.5F, -3.1416F, 0.0F, -2.3562F));
		root.addOrReplaceChild("TailPlane1", CubeListBuilder.create().texOffs(-12, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.5F, 3.1416F, 0.0F, 2.3562F));
		root.addOrReplaceChild("GoldBand", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -11.0F, -3.1416F, 0.0F, 3.1416F));
		root.addOrReplaceChild("FrontSlope", CubeListBuilder.create().texOffs(16, 16).addBox(-1.0F, -0.975F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.3F, 11.7F, 2.7925F, 0.0F, 3.1416F));
		root.addOrReplaceChild("BackSlope", CubeListBuilder.create().texOffs(0, 6).addBox(-1.0F, -0.975F, -3.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, 6.7F, -2.9234F, 0.0F, -3.1416F));
		root.addOrReplaceChild("Nose", CubeListBuilder.create().texOffs(17, 25).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.75F, 13.1F, -3.0543F, 0.0F, 3.1416F));
		root.addOrReplaceChild("Rear", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -2.0F, -3.1416F, 0.0F, 3.1416F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Broomstick broom, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}