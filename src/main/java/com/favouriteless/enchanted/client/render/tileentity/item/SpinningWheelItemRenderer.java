/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.client.render.tileentity.item;

import com.favouriteless.enchanted.client.render.tileentity.SpinningWheelRenderer;
import com.favouriteless.enchanted.common.tileentity.SpinningWheelBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;

public class SpinningWheelItemRenderer extends BlockEntityWithoutLevelRenderer {

	private static SpinningWheelBlockEntity dummyTe = null;
	private static SpinningWheelRenderer dummyRenderer = null;

	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		if(dummyTe == null)
			dummyTe = new SpinningWheelBlockEntity();
		if(dummyRenderer == null)
			dummyRenderer = new SpinningWheelRenderer(BlockEntityRenderDispatcher.instance);

		matrixStack.pushPose();
		dummyRenderer.render(dummyTe, 0.0F, matrixStack, buffer, combinedLight, combinedOverlay);
		matrixStack.popPose();
	}
}
