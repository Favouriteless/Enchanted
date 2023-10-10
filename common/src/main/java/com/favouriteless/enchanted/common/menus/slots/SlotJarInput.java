package com.favouriteless.enchanted.common.menus.slots;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.world.item.ItemStack;

public class SlotJarInput extends SlotItemHandler {

	public SlotJarInput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return (stack.getItem() == EnchantedItems.CLAY_JAR.get());
	}

}
