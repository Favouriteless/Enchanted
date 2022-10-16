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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.*;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public class ArtichokeSeedsItem extends ItemNameBlockItem {
    
    public ArtichokeSeedsItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    public InteractionResult useOn(UseOnContext pContext) {
        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level pWorldIn, Player pPlayerIn, InteractionHand pHandIn) {
        BlockHitResult blockraytraceresult = getPlayerPOVHitResult(pWorldIn, pPlayerIn, ClipContext.Fluid.SOURCE_ONLY);
        BlockHitResult blockraytraceresult1 = blockraytraceresult.withPosition(blockraytraceresult.getBlockPos().above());
        InteractionResult actionresulttype = super.useOn(new UseOnContext(pPlayerIn, pHandIn, blockraytraceresult1));
        return new InteractionResultHolder<>(actionresulttype, pPlayerIn.getItemInHand(pHandIn));
    }
}