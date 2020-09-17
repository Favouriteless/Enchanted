package com.favouriteless.magicraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

public class Altar extends Block {

    public static final BooleanProperty JOINED = BooleanProperty.create("joined"); // this is the property itself

    public Altar(Block.Properties builder) {
        super(builder);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(JOINED, false);
    }
    /* I wont do logic for you but this is called when the block is placed and needs to return if it is joined or not.
    it needs to detect a 2x3 area of altar blocks, if so return with JOINED true, else JOINED false
     */

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(JOINED);
    }



}
