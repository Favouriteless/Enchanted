package com.favouriteless.enchanted.common.blocks.crops;

import com.favouriteless.enchanted.core.init.EnchantedItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CropsBlockAgeFive extends CropsBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 11.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public CropsBlockAgeFive(AbstractBlock.Properties properties) {
        super(properties);
    }


    public VoxelShape getShape(BlockState pState, IBlockReader pWorldIn, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE_BY_AGE[pState.getValue(this.getAgeProperty())];
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected int getBonemealAgeIncrease(World pWorldIn) {
        return MathHelper.nextInt(pWorldIn.random, 1, 3);
    }
}
