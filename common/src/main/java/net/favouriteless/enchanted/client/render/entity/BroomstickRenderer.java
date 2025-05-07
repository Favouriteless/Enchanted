package net.favouriteless.enchanted.client.render.entity;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import net.favouriteless.enchanted.client.render.model.entity.BroomstickModel;
import net.favouriteless.enchanted.common.entities.Broomstick;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BroomstickRenderer extends EntityRenderer<Broomstick> {

	public static final ResourceLocation TEXTURE = Enchanted.id("textures/entity/broomstick.png");
	protected final BroomstickModel model;

	public BroomstickRenderer(Context context) {
		super(context);
		model = new BroomstickModel(context.bakeLayer(ModelLayerLocations.BROOMSTICK));
	}

	@Override
	public void render(Broomstick broomstick, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();

		poseStack.translate(0.0D, 0.7D, 0.0D);
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
		poseStack.mulPose(Axis.XP.rotationDegrees(180.0F - Mth.lerp(partialTicks, broomstick.xRotO, broomstick.getXRot())));

		float f = broomstick.getHurtTime() - partialTicks;
		float f1 = broomstick.getDamage() - partialTicks;
		if (f1 < 0.0F)
			f1 = 0.0F;
		if (f > 0.0F)
			poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float)broomstick.getHurtDir()));

		VertexConsumer buffer = bufferSource.getBuffer(model.renderType(getTextureLocation(broomstick)));
		model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
		poseStack.popPose();

		super.render(broomstick, yaw, partialTicks, poseStack, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(Broomstick entity) {
		return TEXTURE;
	}

}