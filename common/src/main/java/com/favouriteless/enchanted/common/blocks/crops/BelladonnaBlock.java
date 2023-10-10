package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.world.level.ItemLike;

public class BelladonnaBlock extends CropsBlockAgeFive {

    public BelladonnaBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EnchantedItems.BELLADONNA_SEEDS.get();
    }

}
