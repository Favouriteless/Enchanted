package com.favouriteless.enchanted.common.menus.slots;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class SlotFuel extends SlotItemHandler {

	public SlotFuel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return (ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0);
	}
}