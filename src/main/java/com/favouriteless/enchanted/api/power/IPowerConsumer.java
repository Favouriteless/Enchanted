/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.api.power;

import com.favouriteless.enchanted.common.altar.SimplePowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
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
     * @return The {@link IPowerPosHolder} object containing this power consumer's positions.
     */
    @NotNull IPowerConsumer.IPowerPosHolder getPosHolder();



    /**
     * Holds and sorts the positions of every {@link IPowerProvider}
     * this {@link IPowerConsumer} is subscribed to.
     *
     * <p>See {@link SimplePowerPosHolder} for the "default" implementation
     * which sorts the provided positions by proximity.</p>
     */
    interface IPowerPosHolder extends INBTSerializable<ListTag> {

        /**
         * <p>IMPORTANT: {@link IPowerProvider}s do not need to notify their subscribers when they are removed, you
         * should check that it still exists before trying to consume power. See
         * {@link PowerHelper#tryGetPowerProvider(Level, IPowerPosHolder)} for an example implementation of trying to grab a
         * provider.</p>
         *
         * @return List of the BlockPos of every AltarBlockEntity this {@link IPowerConsumer} is subscribed to.
         */
        List<BlockPos> getPositions();

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
