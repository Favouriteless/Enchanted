package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.world.level.ItemLike;

public class WolfsbaneBlock extends CropsBlockAgeFive {

    public WolfsbaneBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EnchantedItems.WOLFSBANE_SEEDS.get();
    }

}
