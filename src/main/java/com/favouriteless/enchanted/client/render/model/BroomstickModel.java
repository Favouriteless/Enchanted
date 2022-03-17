package com.favouriteless.enchanted.client.render.model;

import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BroomstickModel extends EntityModel<BroomstickEntity> {

	private final ModelRenderer bb_main;

	private final ModelRenderer Nose;
	private final ModelRenderer FrontSlope;
	private final ModelRenderer BackSlope;
	private final ModelRenderer Rear;

	private final ModelRenderer TailPlane2;
	private final ModelRenderer TailPlane1;
	private final ModelRenderer GoldBand;

	public BroomstickModel() {
		texWidth = 32;
		texHeight = 32;

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 0.0F, 0.0F);


		TailPlane2 = new ModelRenderer(this);
		TailPlane2.setPos(0.0F, 0.0F, -8.5F);
		bb_main.addChild(TailPlane2);
		setRotationAngle(TailPlane2, -3.1416F, 0.0F, -2.3562F);
		TailPlane2.texOffs(-12, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 12.0F, 0.0F, false);

		TailPlane1 = new ModelRenderer(this);
		TailPlane1.setPos(0.0F, 0.0F, -8.5F);
		bb_main.addChild(TailPlane1);
		setRotationAngle(TailPlane1, 3.1416F, 0.0F, 2.3562F);
		TailPlane1.texOffs(-12, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 0.0F, 12.0F, 0.0F, false);

		GoldBand = new ModelRenderer(this);
		GoldBand.setPos(0.0F, 0.0F, -11.0F);
		bb_main.addChild(GoldBand);
		setRotationAngle(GoldBand, -3.1416F, 0.0F, 3.1416F);
		GoldBand.texOffs(24, 0).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

		FrontSlope = new ModelRenderer(this);
		FrontSlope.setPos(0.0F, 0.3F, 11.7F);
		bb_main.addChild(FrontSlope);
		setRotationAngle(FrontSlope, 2.7925F, 0.0F, 3.1416F);
		FrontSlope.texOffs(16, 16).addBox(-1.0F, -0.975F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

		BackSlope = new ModelRenderer(this);
		BackSlope.setPos(0.0F, 0.6F, 6.7F);
		bb_main.addChild(BackSlope);
		setRotationAngle(BackSlope, -2.9234F, 0.0F, -3.1416F);
		BackSlope.texOffs(0, 6).addBox(-1.0F, -0.975F, -3.0F, 2.0F, 2.0F, 6.0F, 0.0F, false);

		Nose = new ModelRenderer(this);
		Nose.setPos(0.0F, -0.75F, 13.1F);
		bb_main.addChild(Nose);
		setRotationAngle(Nose, -3.0543F, 0.0F, 3.1416F);
		Nose.texOffs(17, 25).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 5.0F, 0.0F, false);

		Rear = new ModelRenderer(this);
		Rear.setPos(0.0F, 0.0F, -2.0F);
		bb_main.addChild(Rear);
		setRotationAngle(Rear, -3.1416F, 0.0F, 3.1416F);
		Rear.texOffs(0, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 2.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(BroomstickEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}