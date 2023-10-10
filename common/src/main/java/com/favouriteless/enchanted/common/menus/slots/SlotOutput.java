package com.favouriteless.enchanted.common.menus.slots;

import net.minecraft.world.item.ItemStack;

public class SlotOutput extends SlotItemHandler {

	public SlotOutput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}
