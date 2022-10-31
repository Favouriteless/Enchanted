/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.blocks.chalk.AbstractChalkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class ChalkItem extends BlockItem {

    private final AbstractChalkBlock chalkBlock;

    public ChalkItem(Block block, Properties properties) {
        super(block, properties);
        if(block instanceof AbstractChalkBlock) {
            chalkBlock = (AbstractChalkBlock) block;
        } else {
            chalkBlock = null;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        if(context.getClickedFace() == Direction.UP) {
            BlockPos targetLocation = context.getClickedPos().above();

            if (context.getLevel().getBlockState(targetLocation).isAir()) {
                if (chalkBlock.canSurvive(chalkBlock.defaultBlockState(),context.getLevel(), targetLocation)) {
                    if(!context.getLevel().isClientSide()) {
                        context.getLevel().setBlockAndUpdate(context.getClickedPos().offset(0, 1, 0), chalkBlock.getStateForPlacement(new BlockPlaceContext(context)));
                    }
                    context.getLevel().playSound(context.getPlayer(), context.getClickedPos().offset(0, 1, 0), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    context.getPlayer().getItemInHand(context.getHand()).hurtAndBreak(1, context.getPlayer(), (p_220038_0_) -> { });

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }



}
