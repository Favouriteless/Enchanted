package com.favouriteless.enchanted.client.render.entity;


import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.render.model.BroomstickModel;
import com.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import com.favouriteless.enchanted.common.entities.Broomstick;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BroomstickRenderer extends EntityRenderer<Broomstick> {

	public static final ResourceLocation TEXTURE = Enchanted.location("textures/entity/broomstick.png");
	protected final BroomstickModel model;

	public BroomstickRenderer(Context context) {
		super(context);
		model = new BroomstickModel(context.bakeLayer(ModelLayerLocations.BROOMSTICK));
	}

	@Override
	public void render(Broomstick broomstick, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();

		poseStack.translate(0.0D, 0.7D, 0.0D);
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
		poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F - Mth.lerp(partialTicks, broomstick.xRotO, broomstick.getXRot())));

		float f = broomstick.getHurtTime() - partialTicks;
		float f1 = broomstick.getDamage() - partialTicks;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}
		if (f > 0.0F) {
			poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)broomstick.getHurtDir()));
		}

		VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(broomstick)));
		this.model.renderToBuffer(poseStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();

		super.render(broomstick, yaw, partialTicks, poseStack, buffer, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(Broomstick pEntity) {
		return TEXTURE;
	}

}