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

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.blocks.chalk.AbstractChalkBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;

public class ChalkItem extends Item {

    private final AbstractChalkBlock chalkBlock;

    public ChalkItem(Block block, Properties properties) {
        super(properties);
        if(block instanceof AbstractChalkBlock) {
            chalkBlock = (AbstractChalkBlock) block;
        } else {
            chalkBlock = null;
        }
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        if(context.getClickedFace() == Direction.UP) {
            BlockPos targetLocation = context.getClickedPos().above();

            if (context.getLevel().getBlockState(targetLocation).isAir()) {
                if (chalkBlock.canSurvive(chalkBlock.defaultBlockState(),context.getLevel(), targetLocation)) {

                    if(!context.getLevel().isClientSide()) {
                        context.getLevel().setBlock(context.getClickedPos().offset(0, 1, 0), Objects.requireNonNull(chalkBlock.getStateForPlacement(new BlockItemUseContext(context))), Constants.BlockFlags.DEFAULT);
                    }
                    context.getLevel().playSound(context.getPlayer(), context.getClickedPos().offset(0, 1, 0), SoundEvents.STONE_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    Objects.requireNonNull(context.getPlayer()).getItemInHand(context.getHand()).hurtAndBreak(1, context.getPlayer(), (p_220038_0_) -> { });

                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.FAIL;
    }

}
