package com.favouriteless.enchanted.common.menus.slots;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class JarInputSlot extends Slot {

	public JarInputSlot(Container container, int index, int x, int y) {
		super(container, index, x, y);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return stack.getItem() == EnchantedItems.CLAY_JAR.get();
	}

}
