package com.favouriteless.enchanted.common.altar;

import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.stateobserver.api.StateChangeSet.StateChange;
import com.favouriteless.stateobserver.api.StateObserver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * StateObserver implementation for {@link AltarBlockEntity}. This is used to notify every nearby {@link IPowerConsumer}
 * of the {@link com.favouriteless.enchanted.api.power.IPowerProvider} near them. Changes to the power/upgrades are also calculated in this StateObserver.
 */
public class AltarStateObserver extends StateObserver {

    public AltarStateObserver(Level level, BlockPos pos, int xRadius, int yRadius, int zRadius) {
        super(level, pos, xRadius, yRadius, zRadius);
    }

    @Override
    protected void handleChanges() {
        if(!getLevel().isClientSide) {
            BlockEntity be = getLevel().getBlockEntity(getPos());
            if (be instanceof AltarBlockEntity altar) { // Only apply this StateObserver to altars.

                for (StateChange change : getStateChangeSet().getChanges()) { // For all changes
                    if (altar.posWithinRange(change.pos(), EnchantedConfig.ALTAR_RANGE.get())) { // Change is relevant
                        if(!change.oldState().is(change.newState().getBlock())) { // Block actually changed
                            if(getLevel().getBlockEntity(change.pos()) instanceof IPowerConsumer consumer)
                                consumer.getPosHolder().add(getPos()); // Subscribe power consumer to this Altar if present.

                            altar.removeBlock(change.oldState().getBlock());
                            altar.addBlock(change.newState().getBlock());
                        }
                        if(altar.posIsUpgrade(change.pos())) {
                            altar.removeUpgrade(change.oldState().getBlock());
                            altar.addUpgrade(change.newState().getBlock());
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onRemove() {

    }

}
