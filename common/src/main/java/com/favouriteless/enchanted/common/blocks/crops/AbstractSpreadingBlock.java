package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public abstract class AbstractSpreadingBlock extends Block {

    public AbstractSpreadingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if(!canSpreadOn(level.getBlockState(pos.below()).getBlock()))
            return;
        if (random.nextInt(25) == 0) {
            int i = 5;

            for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (level.getBlockState(blockpos).is(this)) {
                    --i;
                    if (i <= 0)
                        return;

                }
            }

            BlockPos randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

            for(int k = 0; k < 4; ++k) {
                if (level.isEmptyBlock(randomPos) && state.canSurvive(level, randomPos))
                    pos = randomPos;

                randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }

            if (level.isEmptyBlock(randomPos) && state.canSurvive(level, randomPos))
                level.setBlock(randomPos, state, 2);
        }

    }

    public boolean canSpreadOn(Block block) {
        return true;
    }
}
