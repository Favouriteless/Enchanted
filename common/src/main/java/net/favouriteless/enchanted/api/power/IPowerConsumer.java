package net.favouriteless.enchanted.api.power;

import net.favouriteless.enchanted.api.ISerializable;
import net.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a {@link BlockEntity} which consumes magical power. Every {@link BlockEntity} which needs to consume power
 * from any {@link IPowerProvider} should implement this.
 *
 * <p>A {@link BlockEntity} implementing {@link IPowerConsumer} will be automatically notified of nearby
 * {@link IPowerProvider}s, but it should save/load it's {@link IPowerPosHolder}.</p>
 */
public interface IPowerConsumer {

    /**
     * @return The {@link IPowerPosHolder} containing this power consumer's positions.
     */
    @NotNull IPowerPosHolder getPosHolder();



    /**
     * Holds and sorts the positions of every {@link IPowerProvider}
     * this {@link IPowerConsumer} is subscribed to.
     *
     * <p>See {@link SimplePowerPosHolder} for the "default" implementation
     * which sorts the provided positions by proximity.</p>
     */
    interface IPowerPosHolder extends ISerializable<CompoundTag> {

        /**
         * <p><strong>IMPORTANT:</strong> {@link IPowerProvider}s do not need to notify their subscribers when they are removed, you
         * should check that it still exists before trying to consume power. See
         * {@link PowerHelper#tryGetProvider(Level, IPowerPosHolder)} for an example implementation of trying to grab a
         * provider.</p>
         *
         * @return List of the BlockPos of every AltarBlockEntity this {@link IPowerConsumer} is subscribed to.
         */
        @NotNull List<BlockPos> getPositions();

        /**
         * Remove a {@link BlockPos} from the list of available altar positions.
         * @param altarPos position of the altar being removed from this holder.
         */
        void remove(BlockPos altarPos);

        /**
         * Add a {@link BlockPos} to the list of available altar positions.
         * @param altarPos position of the altar being added to this holder.
         */
        void add(BlockPos altarPos);

    }

}
