package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.world.level.ItemLike;

public class SnowbellBlock extends CropsBlockAgeFive {

    public SnowbellBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EnchantedItems.SNOWBELL_SEEDS.get();
    }

}
