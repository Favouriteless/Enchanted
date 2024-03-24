package com.favouriteless.enchanted.common.menus.slots;

import com.favouriteless.enchanted.common.util.ItemStackHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FuelSlot extends Slot {

	public FuelSlot(Container container, int index, int x, int y) {
		super(container, index, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return ItemStackHelper.isFuel(stack) || stack.is(Items.BUCKET);
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack);
	}

}