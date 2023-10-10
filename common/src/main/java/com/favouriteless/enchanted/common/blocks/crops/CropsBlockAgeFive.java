package com.favouriteless.enchanted.common.blocks.crops;

import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CropsBlockAgeFive extends CropBlock {

    public static final IntegerProperty AGE_FIVE = IntegerProperty.create("age", 0, 4);

    public CropsBlockAgeFive(Properties properties) {
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
    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE_FIVE);
    }
}
