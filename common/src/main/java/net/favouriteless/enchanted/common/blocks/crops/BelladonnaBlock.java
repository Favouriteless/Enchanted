package net.favouriteless.enchanted.common.blocks.crops;

import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.world.level.ItemLike;

public class BelladonnaBlock extends CropsBlockAgeFive {

    public BelladonnaBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EItems.BELLADONNA_SEEDS.get();
    }

}
