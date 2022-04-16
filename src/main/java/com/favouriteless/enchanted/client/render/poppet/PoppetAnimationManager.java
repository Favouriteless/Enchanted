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

package com.favouriteless.enchanted.client.render.poppet;

import com.favouriteless.enchanted.common.items.poppets.PoppetUtils.PoppetResult;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PoppetAnimationManager {

	private static final List<AbstractPoppetAnimation> ACTIVE_ANIMATIONS = new ArrayList<>();

	/**
	 * Starts the corresponding animation for result with the given item
	 * @param result
	 * @param itemStack
	 */
	public static void startAnimation(PoppetResult result, ItemStack itemStack) {
		switch(result) {
			case SUCCESS:
				startAnimation(new PoppetAnimationSuccess(itemStack));
				break;
			case SUCCESS_BREAK:
				startAnimation(new PoppetAnimationSuccess(itemStack));
				break;
			case FAIL:
				break;
		}
	}

	public static void startAnimation(AbstractPoppetAnimation animation) {
		ACTIVE_ANIMATIONS.add(animation);
	}

	public static void tick() {
		for(AbstractPoppetAnimation animation : ACTIVE_ANIMATIONS) {
			animation.tick();
		}
		ACTIVE_ANIMATIONS.removeIf((anim) -> anim.ticks <= 0);
	}

	public static void render(MatrixStack matrixStack, float partialTicks, int widthScaled, int heightScaled) {
		for(AbstractPoppetAnimation animation : ACTIVE_ANIMATIONS) {
			animation.render(matrixStack, partialTicks, widthScaled, heightScaled);
		}
	}

}
