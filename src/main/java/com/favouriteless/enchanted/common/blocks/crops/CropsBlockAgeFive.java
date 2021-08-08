package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class CropsBlockAgeFive extends CropsBlock {

    public static final IntegerProperty AGE_FIVE = IntegerProperty.create("age", 0, 4);

    public CropsBlockAgeFive(AbstractBlock.Properties properties) {
        super(properties);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_FIVE;
    }

    @Override
    public int getMaxAge() {
        return 4;
    }

    @Override
    protected int getBonemealAgeIncrease(World pWorldIn) {
        return MathHelper.nextInt(pWorldIn.random, 1, 3);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE_FIVE);
    }
}
