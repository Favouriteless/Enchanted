package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.core.init.EnchantedItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class ArtichokeBlock extends CropsBlockAgeFive {

    public ArtichokeBlock(Properties properties) {
        super(properties);
    }

    protected IItemProvider getBaseSeedId() {
        return EnchantedItems.ARTICHOKE_SEEDS.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, IBlockReader pWorldIn, BlockPos pPos) {
        FluidState fluidstate = pWorldIn.getFluidState(pPos);
        FluidState fluidstate1 = pWorldIn.getFluidState(pPos.above());
        return fluidstate.getType() == Fluids.WATER && fluidstate.isSource() && fluidstate1.getType() == Fluids.EMPTY;
    }

    @Override
    public boolean canSurvive(BlockState pState, IWorldReader pWorldIn, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        return this.mayPlaceOn(pWorldIn.getBlockState(blockpos), pWorldIn, blockpos);
    }

}
