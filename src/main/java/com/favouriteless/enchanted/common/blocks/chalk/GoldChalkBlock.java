/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.blocks.chalk;

import com.favouriteless.enchanted.common.init.EnchantedTileEntityTypes;
import com.favouriteless.enchanted.common.tileentity.ChalkGoldTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import java.util.Random;

public class GoldChalkBlock extends AbstractChalkBlock {

    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, 3);
    public static final Random random = new Random();

    public GoldChalkBlock() {
        super();
        this.registerDefaultState(getStateDefinition().any().setValue(GLYPH, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return EnchantedTileEntityTypes.CHALK_GOLD.get().create();
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState != pNewState) {
            ChalkGoldTileEntity te = (ChalkGoldTileEntity)pLevel.getBlockEntity(pPos);
            te.clearRite();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLYPH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(GLYPH, random.nextInt(4));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ((ChalkGoldTileEntity) world.getBlockEntity(pos)).execute(state, world, pos, player, hand, hit);
        return InteractionResult.SUCCESS;
    }

}