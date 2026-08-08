package net.favouriteless.enchanted.common.enchanted.circle_magic.rites;

import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class TransposeCasterWaystoneRite extends TransposeEntityRite {

    public TransposeCasterWaystoneRite(BaseRiteParams baseParams, RiteParams params) {
        super(baseParams, params);
    }

    @Override
    protected Entity getTransposee(RiteParams params) {
        return findEntity(params.caster);
    }

    @Override
    protected void findTargetLocation(RiteParams params) {
        for (ItemStack item : params.consumedItems) {
            if (item.has(EDataComponents.LEVEL_KEY.get()) && item.has(EDataComponents.BLOCK_POS.get())) {
                targetLevel = level.getServer().getLevel(item.get(EDataComponents.LEVEL_KEY.get()));
                targetPos = item.get(EDataComponents.BLOCK_POS.get());
                return;
            }
        }
    }

}
