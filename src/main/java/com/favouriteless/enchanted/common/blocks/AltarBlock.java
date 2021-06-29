package com.favouriteless.enchanted.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

public class AltarBlock extends Block {

    public static final BooleanProperty JOINED = BooleanProperty.create("joined");

    public AltarBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
        );
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(JOINED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(JOINED);
    }



}
