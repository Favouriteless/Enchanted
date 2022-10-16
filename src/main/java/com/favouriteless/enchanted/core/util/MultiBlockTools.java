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

import hellfirepvp.observerlib.api.ObserverHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class MultiBlockTools {

    // Return true on success;
    public static boolean breakMultiblock(IMultiBlockType type, Level world, BlockPos pos, BlockState state) {
        // First find the bottom left position of our multiblock
        BlockPos bottomLeft = type.getBottomLowerLeft(world, pos, state);
        ObserverHelper.getHelper().removeObserver(world, bottomLeft);
        if (bottomLeft != null) {
            for (int dx = 0 ; dx < type.getWidth() ; dx++) {
                for (int dy = 0 ; dy < type.getHeight() ; dy++) {
                    for (int dz = 0 ; dz < type.getDepth() ; dz++) {
                        BlockPos p = bottomLeft.offset(dx, dy, dz);
                        if(type.isValidFormedBlock(world, bottomLeft, dx, dy, dz)) {
                            type.unformBlock(world, p);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static boolean formMultiblock(IMultiBlockType type, Level world, BlockPos pos) {
        for (int dx = -type.getWidth()+1 ; dx <= 0 ; dx++) {
            for (int dy = -type.getHeight()+1 ; dy <= 0 ; dy++) {
                for (int dz = -type.getDepth()+1 ; dz <= 0 ; dz++) {
                    BlockPos p = pos.offset(dx, dy, dz);
                    if (type.isValidUnformedMultiBlock(world, p)) {
                        type.create(world, pos.offset(dx, dy, dz));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void createMultiblock(IMultiBlockType type, Level world, BlockPos pos) {
        type.create(world, pos);
    }

}