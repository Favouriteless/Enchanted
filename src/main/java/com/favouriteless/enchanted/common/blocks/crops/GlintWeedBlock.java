package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class GlintWeedBlock extends BushBlock {

    public GlintWeedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        return super.canSurvive(pState, pLevel, pPos);
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.random.nextInt(4) == 0 && pLevel.isAreaLoaded(pPos, 4)) {

        }
    }
}
