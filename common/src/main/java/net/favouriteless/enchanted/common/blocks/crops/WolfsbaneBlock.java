package net.favouriteless.enchanted.common.blocks.crops;

import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.world.level.ItemLike;

public class WolfsbaneBlock extends CropBlockAgeFive {

    public WolfsbaneBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EItems.WOLFSBANE_SEEDS.get();
    }

}
