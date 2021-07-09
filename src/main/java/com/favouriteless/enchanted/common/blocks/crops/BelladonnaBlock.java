package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.core.init.EnchantedItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.IItemProvider;

public class BelladonnaBlock extends CropsBlockAgeFive {

    public BelladonnaBlock(AbstractBlock.Properties properties) {
        super(properties);
    }

    protected IItemProvider getBaseSeedId() {
        return EnchantedItems.BELLADONNA_SEEDS.get();
    }

}
