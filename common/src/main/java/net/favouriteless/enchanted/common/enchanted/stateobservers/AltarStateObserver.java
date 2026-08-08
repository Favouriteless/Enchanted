package net.favouriteless.enchanted.common.enchanted.stateobservers;

import net.favouriteless.enchanted.api.altar.PowerConsumer;
import net.favouriteless.enchanted.common.blocks.entity.AltarBlockEntity;
import net.favouriteless.stateobserver.api.StateChangeSet.StateChange;
import net.favouriteless.stateobserver.api.StateObserver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * StateObserver implementation for {@link AltarBlockEntity}. Used to notify altars when a nearby block changes, and to
 * notify {@link PowerConsumer}s that an altar is nearby.
 */
public class AltarStateObserver extends StateObserver {

    public AltarStateObserver(Level level, BlockPos pos, int xRadius, int yRadius, int zRadius) {
        super(level, pos, xRadius, yRadius, zRadius);
    }

    @Override
    protected void handleChanges() {
        if (getLevel().isClientSide) {
            return;
        }

        if (getLevel().getBlockEntity(getPos()) instanceof AltarBlockEntity altar) {
            for (StateChange change : getChangeSet().getChanges()) {
                if (!altar.posWithinRange(change.pos())) {
                    continue;
                }
                if (change.oldState().is(change.newState().getBlock())) {
                    continue;
                }

                if (getLevel().getBlockEntity(change.pos()) instanceof PowerConsumer consumer) {
                    consumer.getPosHolder().add(getPos()); // Subscribe power consumer to this altar if present.
                }

                altar.removeBlock(change.oldState().getBlock());
                altar.addBlock(change.newState().getBlock());

                if (altar.posIsUpgrade(change.pos())) {
                    altar.removeUpgrade(change.oldState().getBlock());
                    altar.addUpgrade(change.newState().getBlock());
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
