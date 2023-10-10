package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;

public class SpinningWheelBlockItem extends BlockItem {

	public SpinningWheelBlockItem(Properties properties) {
		super(EnchantedBlocks.SPINNING_WHEEL.get(), properties);
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		consumer.accept(new IItemRenderProperties() {
			@Override
			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
				return new SpinningWheelItemRenderer();
			}
		});
	}


}
