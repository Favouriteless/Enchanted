package com.favouriteless.enchanted.client.render.model;// Made with Blockbench 4.1.1
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BroomstickModel extends EntityModel<BroomstickEntity> {

	private final ModelRenderer model;

	public BroomstickModel() {
		super(RenderType::entityCutoutNoCull);
		texWidth = 128;
		texHeight = 64;

		model = new ModelRenderer(this);
		model.texOffs(0, 0).addBox(-1.0F, -1.0F, -16.0F, 2.0F, 2.0F, 32.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(BroomstickEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		model.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}