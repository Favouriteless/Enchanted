package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.core.init.EnchantedMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.Direction;
import net.minecraft.block.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ChalkBlockBase extends Block {

    // BASE CLASS FOR CHALK STUFF, DONT CHANGE
    public ChalkBlockBase() {
        super(AbstractBlock.Properties.of(EnchantedMaterials.CHALK)
                .noCollission()
                .strength(0.5f, 0f)
                .sound(SoundType.STONE)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return VoxelShapes.create(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide()) {
            if (!state.canSurvive(worldIn, pos)) {
                worldIn.removeBlock(pos, false);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        return canSurviveOn(worldIn, blockpos, blockstate);
    }

    private boolean canSurviveOn(IBlockReader reader, BlockPos pos, BlockState state) {
        return state.isFaceSturdy(reader, pos, Direction.UP);
    }

}
