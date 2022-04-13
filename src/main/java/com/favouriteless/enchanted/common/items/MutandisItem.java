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

import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

import java.util.Random;

public class MutandisItem extends Item {

    private static final Random RANDOM = new Random();
    private final IOptionalNamedTag<Block> validBlocks;

    public MutandisItem(IOptionalNamedTag<Block> validBlocks, Properties properties) {
        super(properties);
        this.validBlocks = validBlocks;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if (!state.getBlock().is(EnchantedTags.MUTANDIS_BLACKLIST) && state.getBlock().is(validBlocks)) {
            World world = context.getLevel();
            if(!world.isClientSide) {

                BlockState newState = null;
                while(newState == null) {
                    Block newBlock = validBlocks.getRandomElement(RANDOM);
                    if(!newBlock.is(EnchantedTags.MUTANDIS_BLACKLIST)) {
                        newState = newBlock.defaultBlockState();
                    }
                }

                world.setBlockAndUpdate(context.getClickedPos(), newState);
                world.playSound(null, context.getClickedPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if(!context.getPlayer().isCreative()) context.getItemInHand().shrink(1);
                return ActionResultType.CONSUME;
            }
            else {
                for(int i = 0; i < 10; i++) {
                    double dx = context.getClickedPos().getX() + Math.random();
                    double dy = context.getClickedPos().getY() + Math.random();
                    double dz = context.getClickedPos().getZ() + Math.random();
                    world.addParticle(ParticleTypes.WITCH, dx, dy, dz, 0.0D, 0.0D, 0.0D);
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }
}
