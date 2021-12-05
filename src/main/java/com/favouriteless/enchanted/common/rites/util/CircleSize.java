/*
 * Copyright (c) 2021. Favouriteless
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

package com.favouriteless.enchanted.common.rites.util;


import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;


public enum CircleSize {
    SMALL(
            "  XXX  " +
            " X   X " +
            "X     X" +
            "X     X" +
            "X     X" +
            " X   X " +
            "  XXX  "),
    MEDIUM(
            "   XXXXX   " +
            "  X     X  " +
            " X       X " +
            "X         X" +
            "X         X" +
            "X         X" +
            "X         X" +
            "X         X" +
            " X       X " +
            "  X     X  " +
            "   XXXXX   "),
    LARGE(
            "    XXXXXXX    " +
            "   X       X   " +
            "  X         X  " +
            " X           X " +
            "X             X" +
            "X             X" +
            "X             X" +
            "X             X" +
            "X             X" +
            "X             X" +
            "X             X" +
            " X           X " +
            "  X         X  " +
            "   X       X   " +
            "    XXXXXXX    ");

    private final List<BlockPos> circlePoints = new ArrayList<>();

    CircleSize(String circle) {
        int size = (int)Math.sqrt(circle.length());
        int offset = size/2;

        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {
                if(circle.charAt(x + (size*z)) == 'X') {
                    circlePoints.add(new BlockPos(x-offset, 0, z-offset));
                }
            }
        }
    }

    public boolean match(World world, BlockPos centerPos, Block block) {
        for(BlockPos pos : circlePoints) {
            if(!world.getBlockState(centerPos.offset(pos)).is(block)) {
                return false;
            }
        }
        return true;
    }
}
