package com.favouriteless.enchanted.client.render.blockentity.item;

import com.favouriteless.enchanted.common.blockentities.SpinningWheelBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public class SpinningWheelItemRenderer extends BlockEntityWithoutLevelRenderer {

	private static SpinningWheelBlockEntity dummyBe;

	public SpinningWheelItemRenderer() {
		super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
	}


	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if(dummyBe == null)
			dummyBe = new SpinningWheelBlockEntity(BlockPos.ZERO, EnchantedBlocks.SPINNING_WHEEL.get().defaultBlockState());

		poseStack.pushPose();
		Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(dummyBe, poseStack, buffer, packedLight, packedOverlay);
		poseStack.popPose();
	}
}
