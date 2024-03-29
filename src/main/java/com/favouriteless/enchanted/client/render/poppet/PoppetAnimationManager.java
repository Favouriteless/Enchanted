/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.client.render.poppet;

import com.favouriteless.enchanted.common.poppet.PoppetHelper.PoppetResult;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PoppetAnimationManager {

	private static final List<PoppetAnimation> ACTIVE_ANIMATIONS = new ArrayList<>();

	private static final Vector3f DIFFUSE_LIGHT_0 = Util.make(new Vector3f(0.2F, 0.24F, -0.7F), Vector3f::normalize);
	private static final Vector3f DIFFUSE_LIGHT_1 = Util.make(new Vector3f(-0.2F, 0.24F, 0.7F), Vector3f::normalize);

	/**
	 * Starts the corresponding animation for result with the given item
	 * @param result
	 * @param itemStack
	 */
	public static void startAnimation(PoppetResult result, ItemStack itemStack) {
		switch(result) {
			case SUCCESS, SUCCESS_BREAK -> startAnimation(new PoppetAnimation(itemStack, 120));
		}
	}

	public static void startAnimation(PoppetAnimation animation) {
		ACTIVE_ANIMATIONS.add(animation);
	}

	public static void tick() {
		for(PoppetAnimation animation : ACTIVE_ANIMATIONS) {
			animation.tick();
		}
		ACTIVE_ANIMATIONS.removeIf((anim) -> anim.ticks <= 0);
	}

	public static void render(PoseStack matrixStack, float partialTicks, int widthScaled, int heightScaled) {
		RenderSystem.enableDepthTest();
		RenderSystem.disableCull();
		RenderSystem.setupGui3DDiffuseLighting(DIFFUSE_LIGHT_0, DIFFUSE_LIGHT_1);
		for(PoppetAnimation animation : ACTIVE_ANIMATIONS) {
			animation.render(matrixStack, partialTicks, widthScaled, heightScaled);
		}
		RenderSystem.enableCull();
		RenderSystem.disableDepthTest();
	}

}
