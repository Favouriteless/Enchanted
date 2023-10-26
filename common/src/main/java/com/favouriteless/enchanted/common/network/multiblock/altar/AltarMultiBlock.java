package com.favouriteless.enchanted.common.network.multiblock.altar;

import com.favouriteless.enchanted.common.altar.AltarStateObserver;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.network.multiblock.IMultiBlockType;
import com.favouriteless.stateobserver.StateObserverManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AltarMultiBlock implements IMultiBlockType {

    public static AltarMultiBlock INSTANCE = new AltarMultiBlock();

    public boolean isValidFormedBlock(Level level, BlockPos pos, int dx, int dy, int dz) {
        BlockPos p = pos.offset(dx, dy, dz);
        BlockState state = level.getBlockState(p);
        if (isFormedAltar(state)) {
            AltarPartIndex index = state.getValue(AltarBlock.FORMED);
            return index == AltarPartIndex.getIndex(dx, dz);
        }
        return false;
    }

    public boolean isValidUnformedBlock(Level level, BlockPos pos, int dx, int dy, int dz) {
        BlockPos p = pos.offset(dx, dy, dz);
        BlockState state = level.getBlockState(p);
        return isUnformedAltar(state);
    }

    @Override
    public void create(Level world, BlockPos pos) {
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
    public BlockPos getBottomLowerLeft(Level level, BlockPos pos, BlockState state) {
        if(isFormedAltar(state)) {
            AltarPartIndex index = state.getValue(AltarBlock.FORMED);
            return pos.offset(-index.getDx(), -index.getDy(), -index.getDz());
        }
        else {
            return null;
        }
    }

    @Override
    public void unformBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if(state.is(EnchantedBlocks.ALTAR.get())) {
            if(state.getValue(AltarBlock.FORMED) == AltarPartIndex.P000) {
                AltarStateObserver observer = StateObserverManager.getObserver(level, pos, AltarStateObserver.class);
                if(observer != null)
                    StateObserverManager.removeObserver(observer);

                if(level.getBlockEntity(pos) instanceof AltarBlockEntity)
                    level.removeBlockEntity(pos);
            }
            level.setBlockAndUpdate(pos, EnchantedBlocks.ALTAR.get().defaultBlockState());
        }
    }

    @Override
    public void formBlock(Level level, BlockPos pos, int dx, int dy, int dz) {
        formBlock(level, pos, dx, dy, dz, false);
    }

    public void formBlock(Level world, BlockPos pos, int dx, int dy, int dz, boolean facingX) {
        world.setBlockAndUpdate(pos, EnchantedBlocks.ALTAR.get().defaultBlockState().setValue(AltarBlock.FORMED, AltarPartIndex.getIndex(dx, dz)).setValue(AltarBlock.FACING_X, facingX));
    }

    @Override
    public boolean isValidUnformedMultiBlock(Level world, BlockPos pos) {
        return isValidUnformedMultiBlockX(world, pos) || isValidUnformedMultiBlockZ(world, pos);
    }

    private boolean isValidUnformedMultiBlockX(Level world, BlockPos pos) {
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

    private boolean isValidUnformedMultiBlockZ(Level world, BlockPos pos) {
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
    public boolean isValidFormedMultiBlock(Level level, BlockPos pos) {
        if(!isValidFormedBlock(level, pos, 0, 0, 0)) {
            return false;
        }
        int validCount = 0;

        // Positive X gets priority.
        for(int dx = 0; dx < 3; dx++) {
            for(int dz = 0; dz < 2; dz++) {
                if(isValidFormedBlock(level, pos, dx, 0, dz)) {
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
                if(isValidFormedBlock(level, pos, dx, 0, dz)) {
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
        return (state.is(EnchantedBlocks.ALTAR.get()) && state.getValue(AltarBlock.FORMED) != AltarPartIndex.UNFORMED);
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
