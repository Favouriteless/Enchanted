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

package com.favouriteless.enchanted.api.altar;

import com.favouriteless.enchanted.common.altar.SimpleAltarPosHolder;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a {@link BlockEntity} which consumes altar power. Every {@link BlockEntity} which need to consume power
 * from altars should implement this.
 *
 * <p>A {@link BlockEntity} implementing {@link IAltarPowerConsumer} will be automatically notified of nearby Altars.</p>
 */
public interface IAltarPowerConsumer {

    /**
     * @return The {@link IAltarPosHolder} object containing this power consumer's positions.
     */
    @NotNull IAltarPosHolder getAltarPosHolder();



    /**
     * Holds and sorts the positions of every {@link AltarBlockEntity}
     * this {@link IAltarPowerConsumer} is subscribed to.
     *
     * <p>See {@link SimpleAltarPosHolder} for the "default" implementation
     * which sorts the provided positions by proximity.</p>
     */
    interface IAltarPosHolder extends INBTSerializable<ListTag> {

        /**
         * @return List of the BlockPos of every AltarBlockEntity this {@link IAltarPowerConsumer} is subscribed to.
         */
        List<BlockPos> getAltarPositions();

        /**
         * Remove a {@link BlockPos} from the list of available altar positions.
         * @param altarPos position of the altar being removed from this holder.
         */
        void removeAltar(BlockPos altarPos);

        /**
         * Add a {@link BlockPos} to the list of available altar positions.
         * @param altarPos position of the altar being added to this holder.
         */
        void addAltar(BlockPos altarPos);

    }

}
