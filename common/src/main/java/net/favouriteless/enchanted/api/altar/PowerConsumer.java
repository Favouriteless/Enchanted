package net.favouriteless.enchanted.api.altar;

import net.favouriteless.enchanted.api.ISerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

/**
 * Represents a {@link BlockEntity} which consumes magical power. Every {@link BlockEntity} which needs to consume power
 * from any {@link PowerProvider} should implement this.
 *
 * <p>A {@link BlockEntity} implementing {@link PowerConsumer} will be automatically notified of nearby
 * {@link PowerProvider}s, but it should save/load it's {@link PowerPosHolder}.</p>
 */
public interface PowerConsumer {

    /**
     * @return The {@link PowerPosHolder} containing this power consumer's positions.
     */
    PowerPosHolder getPosHolder();



    /**
     * Holds and sorts the positions of every {@link PowerProvider}
     * this {@link PowerConsumer} is subscribed to.
     *
     * <p>See {@link SimplePowerPosHolder} for the "default" implementation
     * which sorts the provided positions by proximity.</p>
     */
    interface PowerPosHolder extends ISerializable<CompoundTag> {

        /**
         * <p><strong>IMPORTANT:</strong> {@link PowerProvider}s do not need to notify their subscribers when they are removed, you
         * should check that it still exists before trying to consume power. See
         * {@link PowerHelper#tryGetProvider(Level, PowerPosHolder)} for an example implementation of trying to grab a
         * provider.</p>
         *
         * @return List of the BlockPos of every AltarBlockEntity this {@link PowerConsumer} is subscribed to.
         */
        List<BlockPos> getPositions();

        /**
         * Remove a {@link BlockPos} from the list of available altar positions.
         * @param pos position of the altar being removed from this holder.
         */
        void remove(BlockPos pos);

        /**
         * Add a {@link BlockPos} to the list of available altar positions.
         * @param pos position of the altar being added to this holder.
         */
        void add(BlockPos pos);

    }

}
