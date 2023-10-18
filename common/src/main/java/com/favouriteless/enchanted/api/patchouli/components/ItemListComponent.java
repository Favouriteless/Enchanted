package com.favouriteless.enchanted.api.patchouli.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class ItemListComponent implements ICustomComponent {

	public boolean startBottom;
	public boolean centered;
	public int itemsPerRow;
	public IVariable itemList;
	public transient List<ItemStack> items;
	private transient int x;
	private transient int y;

	private final transient List<ItemRow> itemRows = new ArrayList<>();

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
		repopulateRows();
	}

	@Override
	public void render(PoseStack matrix, IComponentRenderContext context, float partialTicks, int mouseX, int mouseY) {
		for(ItemRow row : itemRows) {
			row.render(matrix, context, mouseX, mouseY);
		}
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		items = Arrays.asList(lookup.apply(itemList).as(ItemStack[].class));
	}

	public void repopulateRows() {
		this.itemRows.clear();

		int startY = startBottom ? y - ((int)Math.ceil(items.size()/(float)itemsPerRow))*16 : y;
		int index = 0;
		while(index < items.size()) {
			List<ItemStack> subList = items.subList(index, Math.min(items.size(), index+itemsPerRow));
			itemRows.add(new ItemRow(subList, x, startY + (index/itemsPerRow)*16, centered));

			index += itemsPerRow;
		}
	}

	private static class ItemRow {

		private final List<ItemStack> items;
		private final int x;
		private final int y;

		private ItemRow(List<ItemStack> items, int x, int y, boolean centered) {
			this.items = items;
			this.x = centered ? Math.round(x - items.size()*16/2.0F) : x;
			this.y = y;
		}

		private void render(PoseStack matrix, IComponentRenderContext context, int mouseX, int mouseY) {
			int xOffset = 0;
			for(ItemStack stack : items) {
				context.renderItemStack(matrix, x+xOffset, y, mouseX, mouseY, stack);
				xOffset += 16;
			}
		}
	}
}
