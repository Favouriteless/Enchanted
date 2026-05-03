package net.favouriteless.enchanted.common.enchanted.altar;

import net.favouriteless.enchanted.common.blocks.altar.AltarBlock;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Utility class for forming, unforming and checking if a given altar block can form a valid altar.
 * <p>
 * Yes, it's hardcoded and I don't want to hear a single complaint. It doesn't need to be complicated.
 * </p>
 */
public class AltarHelper {

    private static final int LENGTH = 3;
    private static final int WIDTH = 2;

    /**
     * Searches for a valid altar in level which contains pos and forms an altar there if one is found.
     *
     * @param level the level to search in.
     * @param pos the pos of the altar block to search from.
     *
     * @return {@code true} if an altar is formed, otherwise {@code false}.
     */
    public static boolean tryFormAltar(Level level, BlockPos pos) {
        MutableBlockPos mp = pos.mutable();

        // Check is split into the max values for X and Z individually improve performance.
        for(int x = 0; x < LENGTH; x++) {
            for(int z = 0; z < WIDTH; z++) {
                mp.setX(pos.getX() - x).setZ(pos.getZ() - z);
                if(tryFormAltarX(level, mp))
                    return true;
            }
        }

        for(int x = 0; x < WIDTH; x++) {
            for(int z = 0; z < LENGTH; z++) {
                mp.setX(pos.getX() - x).setZ(pos.getZ() - z);
                if(tryFormAltarZ(level, mp))
                    return true;
            }
        }

        return false;
    }

    /**
     * Attempts to unform the altar at the given position.
     *
     * @param level the level to search in.
     * @param pos the pos of core block of the altar.
     * @param state the state of the altar block, in case it was broken.
     */
    public static void tryUnformAltar(Level level, BlockPos pos, BlockState state) {
        BlockPos core = getCorePos(pos, state);
        boolean facingX = state.getValue(AltarBlock.FACING_X);

        setAltar(level, core, facingX, (old, x, z) -> isFormedAltar(old) ? EBlocks.ALTAR.get().defaultBlockState() : null);
    }

    private static boolean tryFormAltarX(Level level, BlockPos pos) {
        return isValidAltar(level, pos, LENGTH, WIDTH) && formAltar(level, pos, true);
    }

    private static boolean tryFormAltarZ(Level level, BlockPos pos) {
        return isValidAltar(level, pos, WIDTH, LENGTH) && formAltar(level, pos, false);
    }

    private static boolean formAltar(Level level, BlockPos pos, boolean facingX) {
        setAltar(level, pos, facingX, (old, x, z) ->
                EBlocks.ALTAR.get().defaultBlockState()
                        .setValue(AltarBlock.PART, AltarPart.getByOffset(x, z))
                        .setValue(AltarBlock.FACING_X, facingX)
        );
        return true;
    }

    private static void setAltar(Level level, BlockPos pos, boolean facingX, StateFunction stateFunction) {
        int xMax;
        int zMax;

        if(facingX) {
            xMax = LENGTH;
            zMax = WIDTH;
        } else {
            xMax = WIDTH;
            zMax = LENGTH;
        }

        MutableBlockPos mp = pos.mutable();

        for(int x = 0; x < xMax; x++) {
            for(int z = 0; z < zMax; z++) {
                mp.setX(pos.getX() + x).setZ(pos.getZ() + z);
                BlockState newState = stateFunction.get(level.getBlockState(mp), x, z);

                if(newState != null)
                    level.setBlockAndUpdate(mp, newState);
            }
        }
    }

    private interface StateFunction {
        BlockState get(BlockState old, int x, int z);
    }

    private static boolean isValidAltar(Level level, BlockPos pos, int xMax, int zMax) {
        MutableBlockPos mp = pos.mutable();

        for(int x = 0; x < xMax; x++) {
            for(int z = 0; z < zMax; z++) {
                mp.setX(pos.getX() + x).setZ(pos.getZ() + z);

                if(!isUnformedAltar(level.getBlockState(mp)))
                    return false;
            }
        }
        return true;
    }

    private static boolean isUnformedAltar(BlockState state) {
        return state.is(EBlocks.ALTAR.get()) && state.getValue(AltarBlock.PART) == AltarPart.UNFORMED;
    }

    private static boolean isFormedAltar(BlockState state) {
        return state.is(EBlocks.ALTAR.get()) && state.getValue(AltarBlock.PART) != AltarPart.UNFORMED;
    }

    public static BlockPos getCorePos(BlockPos pos, BlockState state) {
        AltarPart part = state.getValue(AltarBlock.PART);
        return pos.offset(-part.getX(), -part.getY(), -part.getZ());
    }

}
