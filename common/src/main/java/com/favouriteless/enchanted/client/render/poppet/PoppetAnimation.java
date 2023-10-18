package com.favouriteless.enchanted.client.render.poppet;

import com.favouriteless.enchanted.Enchanted;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class PoppetAnimation {

	private final ItemStack itemStack;
	protected int ticks;

	public PoppetAnimation(ItemStack itemStack, int ticks) {
		this.itemStack = itemStack;
		this.ticks = ticks;
	}

	public void render(PoseStack matrixStack, float partialTicks, int widthScaled, int heightScaled) {
		int ticksLeft = 120 - this.ticks;
		float work = ((float)ticksLeft + partialTicks) / 120.0F; // Work done (0->1)
		float workSq = work*work;
		float workCb = workSq*work;

		float scale = 255.0F * Mth.sin((float)Math.pow(2.05F * work - 0.9F, 7) + 0.5F); // Plug this into a graphing tool to see how it scales

		matrixStack.pushPose();
		Minecraft minecraft = Minecraft.getInstance();

		// Random shake
		if(work > 0.2F && work < 0.55F) {
			int maxOffset = widthScaled > heightScaled ? widthScaled / 80 : heightScaled / 80;
			int offsetOffset = maxOffset/2;
			matrixStack.translate(Enchanted.RANDOM.nextInt(maxOffset)-offsetOffset, Enchanted.RANDOM.nextInt(maxOffset)-offsetOffset, 0);
		}

		matrixStack.translate((float)(widthScaled / 2), (float)(heightScaled / 2), -50.0D);
		matrixStack.scale(scale, -scale, scale); // Renders upside down at a positive scale

		float rotationCurve = 10.25F * workCb*workSq - 24.95F * workSq*workSq + 25.5F * workSq*work - 13.8F * workSq + 4.0F * work;
		float piCurve = rotationCurve * (float)Math.PI;
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(piCurve))));
		matrixStack.mulPose(Vector3f.XP.rotationDegrees(6.0F * Mth.cos(work * 8.0F)));
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(6.0F * Mth.cos(work * 8.0F)));

		MultiBufferSource.BufferSource renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
		minecraft.getItemRenderer().renderStatic(itemStack, TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, 0);

		matrixStack.popPose();
		renderTypeBuffer.endBatch();
	}


	public ItemStack getItem() {
		return itemStack;
	}

	public int getTicks() {
		return ticks;
	}

	public void tick() {
		ticks--;
	}

}
