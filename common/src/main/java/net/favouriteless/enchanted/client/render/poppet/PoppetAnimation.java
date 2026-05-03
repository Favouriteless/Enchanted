package net.favouriteless.enchanted.client.render.poppet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PoppetAnimation {

    private static final int DURATION = 120;

	private final ItemStack item;
	protected int ticks;

	public PoppetAnimation(Item item) {
		this.item = new ItemStack(item);
		this.ticks = DURATION;
	}

	public void render(PoseStack poseStack, float partialTicks, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

		float work = (DURATION - ticks + partialTicks) / 120.0F; // Work done (0->1)
		float workSq = work*work;
		float workCb = workSq*work;

		poseStack.pushPose();

		if(work > 0.2F && work < 0.55F) { // Random shake
			int max = width > height ? width / 80 : height / 80;
			int offset = max/2;
			poseStack.translate(RandomUtils.nextInt(max) - offset, RandomUtils.nextInt(max) - offset, 0);
		}

        float c = 2.05F * work - 0.9F;
        c = c * c * c * c * c * c * c; // Power of 7
        float scale = 255.0F * Mth.sin(c + 0.5F); // Plug this into a graphing tool to see how it scales
        float rotation = (10.25F * workCb*workSq - 24.95F * workSq*workSq + 25.5F * workSq*work - 13.8F * workSq + 4.0F * work) * Mth.PI;

		poseStack.translate(width / 2.0F, height / 2.0F, -50.0D);
		poseStack.scale(scale, -scale, scale); // Renders upside down at a positive scale

		poseStack.mulPose(Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin(rotation))));
		poseStack.mulPose(Axis.XP.rotationDegrees(6.0F * Mth.cos(work * 8.0F)));
		poseStack.mulPose(Axis.ZP.rotationDegrees(6.0F * Mth.cos(work * 8.0F)));

		itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, 0xF000F0, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, mc.level, 0);

		poseStack.popPose();
		bufferSource.endBatch();
	}

	public void tick() {
		ticks--;
	}

}
