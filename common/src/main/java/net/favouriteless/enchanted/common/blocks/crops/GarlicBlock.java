package net.favouriteless.enchanted.common.blocks.crops;

import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.world.level.ItemLike;

public class GarlicBlock extends CropBlockAgeFive {

    public GarlicBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EItems.GARLIC.get();
    }

}
