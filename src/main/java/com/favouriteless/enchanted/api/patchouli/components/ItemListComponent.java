/*
 * Copyright (c) 2021. Favouriteless
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

package com.favouriteless.enchanted.api.patchouli.components;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.function.UnaryOperator;

public class ItemListComponent implements ICustomComponent {

	public boolean startBottom;
	public boolean centered;
	public int itemsPerRow;
	public IVariable itemList;
	public transient ItemStack[] items;
	private transient int x;
	private transient int y;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(MatrixStack matrix, IComponentRenderContext context, float partialticks, int mouseX, int mouseY) {
		int itemsRendered = 0;
		int width = Math.min(itemsPerRow, items.length)*16;
		int xOffset = centered ? -(width/2): 0;
		int yMult = startBottom ? -1 : 1;
		int yOffset = startBottom ? -16 : 0;

		for(ItemStack item : items) {
			context.renderItemStack(matrix, x+((itemsRendered%itemsPerRow) * 16)+xOffset, y+((itemsRendered/itemsPerRow) * 16)*yMult+yOffset, mouseX, mouseY, item);
			itemsRendered++;
		}
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		items = lookup.apply(itemList).as(ItemStack[].class);
	}
}
