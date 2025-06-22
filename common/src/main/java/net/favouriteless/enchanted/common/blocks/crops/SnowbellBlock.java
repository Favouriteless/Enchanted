package net.favouriteless.enchanted.common.blocks.crops;

import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.world.level.ItemLike;

public class SnowbellBlock extends CropBlockAgeFive {

    public SnowbellBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EItems.SNOWBELL_SEEDS.get();
    }

}
