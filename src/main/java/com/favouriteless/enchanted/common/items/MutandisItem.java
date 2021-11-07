/*
 * Copyright (c) 2021. Favouriteless
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

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

import java.util.Arrays;
import java.util.List;

public class MutandisItem extends Item {

    public static List<Block> SWAPPABLE_BLOCKS = Arrays.asList(
            Blocks.GRASS,
            Blocks.BROWN_MUSHROOM,
            Blocks.RED_MUSHROOM,
            Blocks.SUGAR_CANE,
            Blocks.CACTUS,
            Blocks.OAK_SAPLING,
            Blocks.BIRCH_SAPLING,
            Blocks.ACACIA_SAPLING,
            Blocks.SPRUCE_SAPLING,
            Blocks.JUNGLE_SAPLING,
            Blocks.DARK_OAK_SAPLING,
            Blocks.DANDELION,
            Blocks.POPPY,
            Blocks.BLUE_ORCHID,
            Blocks.ALLIUM,
            Blocks.AZURE_BLUET,
            Blocks.ORANGE_TULIP,
            Blocks.PINK_TULIP,
            Blocks.RED_TULIP,
            Blocks.WHITE_TULIP,
            Blocks.OXEYE_DAISY,
            Blocks.CORNFLOWER,
            Blocks.LILY_OF_THE_VALLEY,
            EnchantedBlocks.ALDER_SAPLING.get(),
            EnchantedBlocks.HAWTHORN_SAPLING.get(),
            EnchantedBlocks.ROWAN_SAPLING.get(),
            EnchantedBlocks.EMBER_MOSS.get(),
            EnchantedBlocks.GLINT_WEED.get(),
            EnchantedBlocks.BELLADONNA.get(),
            EnchantedBlocks.MANDRAKE.get(),
            EnchantedBlocks.SPANISH_MOSS.get(),
            EnchantedBlocks.SNOWBELL.get(),
            EnchantedBlocks.ARTICHOKE.get()
    );

    public MutandisItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if(!context.getLevel().isClientSide) {
            if (SWAPPABLE_BLOCKS.contains(state.getBlock())) {
                context.getLevel().setBlockAndUpdate(context.getClickedPos(), SWAPPABLE_BLOCKS.get(random.nextInt(SWAPPABLE_BLOCKS.size())).defaultBlockState());
                return ActionResultType.CONSUME;
            }
            return ActionResultType.FAIL;
        }

        return ActionResultType.SUCCESS;
    }
}
