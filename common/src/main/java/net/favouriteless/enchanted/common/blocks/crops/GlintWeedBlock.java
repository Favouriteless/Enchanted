package net.favouriteless.enchanted.common.blocks.crops;

import net.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class GlintWeedBlock extends AbstractSpreadingBlock {

    public GlintWeedBlock() {
        super(Properties.copy(Blocks.POPPY).lightLevel((a) -> 14).randomTicks());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState state1 = level.getBlockState(pos.below());
        BlockState state2 = level.getBlockState(pos.above());

        return (!level.isEmptyBlock(pos.below()) && state1.isFaceSturdy(level, pos.below(), Direction.UP) || state1.is(BlockTags.LEAVES) ) ||
                (!level.isEmptyBlock(pos.above()) && state2.isFaceSturdy(level, pos.above(), Direction.DOWN) || state2.is(BlockTags.LEAVES) );
    }

    @Override
    public boolean canSpreadOn(BlockState block) {
        return block.is(EnchantedTags.Blocks.GLINT_WEED_SPREADS_ON);
    }

}
