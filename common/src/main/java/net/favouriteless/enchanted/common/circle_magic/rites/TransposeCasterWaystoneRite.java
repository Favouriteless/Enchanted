package net.favouriteless.enchanted.common.circle_magic.rites;

import net.favouriteless.enchanted.common.init.registry.EItems;
import net.favouriteless.enchanted.common.util.WaystoneHelper;
import net.minecraft.server.level.ServerLevel;
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
        for(ItemStack item : params.consumedItems) {
            if (item.getItem() == EItems.BOUND_WAYSTONE.get()) {
                targetLevel = (ServerLevel)WaystoneHelper.getLevel(level, item);
                targetPos = WaystoneHelper.getPos(item);
                return;
            }
        }
    }

}
