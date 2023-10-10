package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ArtichokeBlock extends CropsBlockAgeFive {

    public ArtichokeBlock(Properties properties) {
        super(properties);
    }

    protected ItemLike getBaseSeedId() {
        return EnchantedItems.ARTICHOKE_SEEDS.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        FluidState clickedState = level.getFluidState(pos);
        FluidState aboveClicked = level.getFluidState(pos.above());
        return clickedState.getType() == Fluids.WATER && clickedState.isSource() && aboveClicked.getType() == Fluids.EMPTY;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        return mayPlaceOn(level.getBlockState(blockpos), level, blockpos);
    }

}
