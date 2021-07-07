package com.favouriteless.enchanted.core.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockTools {

    // Return true on success;
    public static boolean breakMultiblock(IMultiBlockType type, World world, BlockPos pos, BlockState state) {
        // First find the bottom left position of our multiblock
        BlockPos bottomLeft = type.getBottomLowerLeft(world, pos, state);
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

    public static boolean formMultiblock(IMultiBlockType type, World world, BlockPos pos) {
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

    private static void createMultiblock(IMultiBlockType type, World world, BlockPos pos) {
        type.create(world, pos);
    }

}