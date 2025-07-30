package net.favouriteless.enchanted.client.render.poppet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PoppetAnimation {

	private final ItemStack itemStack;
	protected int ticks;

	public PoppetAnimation(ItemStack itemStack, int ticks) {
		this.itemStack = itemStack;
		this.ticks = ticks;
	}

	public void render(PoseStack poseStack, float partialTicks, int widthScaled, int heightScaled) {
		int ticksLeft = 120 - this.ticks;
		float work = ((float)ticksLeft + partialTicks) / 120.0F; // Work done (0->1)
		float workSq = work*work;
		float workCb = workSq*work;

		float scale = 255.0F * Mth.sin((float)Math.pow(2.05F * work - 0.9F, 7) + 0.5F); // Plug this into a graphing tool to see how it scales

		poseStack.pushPose();
		Minecraft minecraft = Minecraft.getInstance();

		// Random shake
		if(work > 0.2F && work < 0.55F) {
			int maxOffset = widthScaled > heightScaled ? widthScaled / 80 : heightScaled / 80;
			int offsetOffset = maxOffset/2;
			poseStack.translate(RandomUtils.nextInt(maxOffset)-offsetOffset, RandomUtils.nextInt(maxOffset)-offsetOffset, 0);
		}

		poseStack.translate(widthScaled / 2.0F, heightScaled / 2.0F, -50.0D);
		poseStack.scale(scale, -scale, scale); // Renders upside down at a positive scale

		float rotationCurve = 10.25F * workCb*workSq - 24.95F * workSq*workSq + 25.5F * workSq*work - 13.8F * workSq + 4.0F * work;
		float piCurve = rotationCurve * (float)Math.PI;
		poseStack.mulPose(Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(piCurve))));
		poseStack.mulPose(Axis.XP.rotationDegrees(6.0F * Mth.cos(work * 8.0F)));
		poseStack.mulPose(Axis.ZP.rotationDegrees(6.0F * Mth.cos(work * 8.0F)));

		MultiBufferSource.BufferSource renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
		minecraft.getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, minecraft.level, 0);

		poseStack.popPose();
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
