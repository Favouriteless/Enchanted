package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import net.minecraft.world.item.BlockItem;

/**
 * <strong>IMPORTANT:</strong> Forge implementation will mixin this to set up it's client render stuff.
 */
public class SpinningWheelBlockItem extends BlockItem {

	public SpinningWheelBlockItem(Properties properties) {
		super(EnchantedBlocks.SPINNING_WHEEL.get(), properties);
	}

}
