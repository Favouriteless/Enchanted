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
import com.favouriteless.enchanted.common.tileentity.SpinningWheelTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class SpinningWheelItemRenderer extends ItemStackTileEntityRenderer {

	private static SpinningWheelTileEntity dummyTe = null;
	private static SpinningWheelRenderer dummyRenderer = null;

	@Override
	public void renderByItem(ItemStack stack, TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if(dummyTe == null)
			dummyTe = new SpinningWheelTileEntity();
		if(dummyRenderer == null)
			dummyRenderer = new SpinningWheelRenderer(TileEntityRendererDispatcher.instance);

		matrixStack.pushPose();
		dummyRenderer.render(dummyTe, 0.0F, matrixStack, buffer, combinedLight, combinedOverlay);
		matrixStack.popPose();
	}
}
