package net.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractSpreadingBlock extends Block {

    public AbstractSpreadingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if(!canSpreadOn(level.getBlockState(pos.below())))
            return;

        if(random.nextInt(25) != 0)
            return;

        int max = 5;
        for(BlockPos p : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
            if(level.getBlockState(p).is(this) && --max <= 0)
                return;
        }

        for(int k = 0; k < 4; ++k) {
            BlockPos p = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            if(level.isEmptyBlock(p) && state.canSurvive(level, p)) {
                level.setBlockAndUpdate(p, state);
                break;
            }
        }
    }

    public boolean canSpreadOn(BlockState block) {
        return true;
    }

}
