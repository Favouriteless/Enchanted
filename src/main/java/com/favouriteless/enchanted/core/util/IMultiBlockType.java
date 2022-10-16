/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.core.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IMultiBlockType {

    /**
     * Return the anchor position for the given formed multiblock. This is the corner at the
     * bottom/lower/left position.
     * WARNING! Implementations of this function can not assume that the given block is
     * actually a valid part of the multiblock!
     * @return anchor position or null in case this is not a valid (formed) multiblock
     */
    @Nullable
    BlockPos getBottomLowerLeft(Level world, BlockPos pos, BlockState state);

    /**
     * Return a given block in the world to its unformed state.
     * This function can assume the given position refers to a valid multiblock part
     */
    void unformBlock(Level world, BlockPos pos);

    /**
     * Convert a given block in the world to its formed state for the given
     * relative position in the multiblock.
     * This function can assume the given position refers to a valid multiblock part
     */
    void formBlock(Level world, BlockPos pos, int dx, int dy, int dz);

    /**
     * Return true if the given position is the bottom/lower/left position
     * of an unformed multiblock. i.e. it is possible to form a multiblock here
     */
    boolean isValidUnformedMultiBlock(Level world, BlockPos pos);

    /**
     * Return true if the given position is the bottom/lower/left position
     * of a formed multiblock
     */
    boolean isValidFormedMultiBlock(Level world, BlockPos pos);

    /**
     * Returns true if the block in pos + dxdyxz is valid as formed for that position
     */
    boolean isValidFormedBlock(Level world, BlockPos pos, int dx, int dy, int dz);

    /**
     * Returns true if the block in pos + dxdyxz is valid as unformed for that position
     */
    boolean isValidUnformedBlock(Level world, BlockPos pos, int dx, int dy, int dz);

    void create(Level world, BlockPos pos);

    /// The dimension of this multiblock type on the X axis
    int getWidth();
    /// The dimension of this multiblock type on the Y axis
    int getHeight();
    /// The dimension of this mulitblock type on the Z axis
    int getDepth();
}