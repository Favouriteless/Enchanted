package com.favouriteless.enchanted.common.multiblock.altar;

import com.favouriteless.enchanted.common.blocks.AltarBlock;
import com.favouriteless.enchanted.core.init.EnchantedBlocks;
import com.favouriteless.enchanted.core.util.IMultiBlockType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AltarMultiBlock implements IMultiBlockType {

    public static AltarMultiBlock INSTANCE = new AltarMultiBlock();

    public boolean isValidFormedBlock(World world, BlockPos pos, int dx, int dy, int dz) {
        BlockPos p = pos.offset(dx, dy, dz);
        BlockState state = world.getBlockState(p);
        if (isFormedAltar(state)) {
            AltarPartIndex index = state.getValue(AltarBlock.FORMED);
            return index == AltarPartIndex.getIndex(dx, dz);
        }
        return false;
    }

    public boolean isValidUnformedBlock(World world, BlockPos pos, int dx, int dy, int dz) {
        BlockPos p = pos.offset(dx, dy, dz);
        BlockState state = world.getBlockState(p);
        return isUnformedAltar(state);
    }

    @Override
    public void create(World world, BlockPos pos) {
        if(isValidUnformedMultiBlockX(world, pos)) {
            for (int dx = 0; dx < 3; dx++) {
                for (int dz = 0; dz < 2; dz++) {
                    if (isValidUnformedBlock(world, pos, dx, 0, dz)) {
                        formBlock(world, pos.offset(dx, 0, dz), dx, 0, dz, true);
                    }
                }
            }
        }
        else {
            for (int dx = 0; dx < 2; dx++) {
                for (int dz = 0; dz < 3; dz++) {
                    if (isValidUnformedBlock(world, pos, dx, 0, dz)) {
                        formBlock(world, pos.offset(dx, 0, dz), dx, 0, dz, false);
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockPos getBottomLowerLeft(World world, BlockPos pos, BlockState state) {
        if(state.is(EnchantedBlocks.ALTAR.get())) {
            AltarPartIndex index = state.getValue(AltarBlock.FORMED);
            return pos.offset(-index.getDx(), -index.getDy(), -index.getDz());
        }
        else {
            return null;
        }
    }

    @Override
    public void unformBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if(state.is(EnchantedBlocks.ALTAR.get())) {
            world.setBlock(pos, state.setValue(AltarBlock.FORMED, AltarPartIndex.UNFORMED), 3);
        }
    }

    @Override
    public void formBlock(World world, BlockPos pos, int dx, int dy, int dz) {
        BlockState state = world.getBlockState(pos);
        if(state.is(EnchantedBlocks.ALTAR.get())) {
            world.setBlock(pos, world.getBlockState(pos).setValue(AltarBlock.FORMED, AltarPartIndex.getIndex(dx, dz)), 3);
        }
    }

    public void formBlock(World world, BlockPos pos, int dx, int dy, int dz, boolean facingX) {
        BlockState state = world.getBlockState(pos);
        if(state.is(EnchantedBlocks.ALTAR.get())) {
            world.setBlock(pos, world.getBlockState(pos).setValue(AltarBlock.FORMED, AltarPartIndex.getIndex(dx, dz)).setValue(AltarBlock.FACING_X, facingX), 3);
        }
    }

    @Override
    public boolean isValidUnformedMultiBlock(World world, BlockPos pos) {
        return isValidUnformedMultiBlockX(world, pos) || isValidUnformedMultiBlockZ(world, pos);
    }

    private boolean isValidUnformedMultiBlockX(World world, BlockPos pos) {
        if(!isValidUnformedBlock(world, pos, 0, 0, 0)) {
            return false;
        }
        int validCount = 0;

        for(int dx = 0; dx < 3; dx++) {
            for(int dz = 0; dz < 2; dz++) {
                if(isValidUnformedBlock(world, pos, dx, 0, dz)) {
                    validCount++;
                }
            }
        }
        return validCount == 6;
    }

    private boolean isValidUnformedMultiBlockZ(World world, BlockPos pos) {
        if(!isValidUnformedBlock(world, pos, 0, 0, 0)) {
            return false;
        }
        int validCount = 0;

        for(int dx = 0; dx < 2; dx++) {
            for(int dz = 0; dz < 3; dz++) {
                if(isValidUnformedBlock(world, pos, dx, 0, dz)) {
                    validCount++;
                }
            }
        }
        return validCount == 6;
    }

    @Override
    public boolean isValidFormedMultiBlock(World world, BlockPos pos) {
        if(!isValidFormedBlock(world, pos, 0, 0, 0)) {
            return false;
        }
        int validCount = 0;

        // Positive X gets priority.
        for(int dx = 0; dx < 3; dx++) {
            for(int dz = 0; dz < 2; dz++) {
                if(isValidFormedBlock(world, pos, dx, 0, dz)) {
                    validCount++;
                }
            }
        }
        if(validCount == 6) {
            return true;
        }
        validCount = 0;
        for(int dx = 0; dx < 2; dx++) {
            for(int dz = 0; dz < 3; dz++) {
                if(isValidFormedBlock(world, pos, dx, 0, dz)) {
                    validCount++;
                }
            }
        }
        return validCount == 6;
    }

    private static boolean isUnformedAltar(BlockState state) {
        return state.is(EnchantedBlocks.ALTAR.get()) && state.getValue(AltarBlock.FORMED) == AltarPartIndex.UNFORMED;
    }

    private static boolean isFormedAltar(BlockState state) {
        return state.is(EnchantedBlocks.ALTAR.get()) && state.getValue(AltarBlock.FORMED) != AltarPartIndex.UNFORMED;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getDepth() {
        return 3;
    }

}
