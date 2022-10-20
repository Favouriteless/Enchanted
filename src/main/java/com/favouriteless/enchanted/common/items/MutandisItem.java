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

import com.favouriteless.enchanted.common.init.EnchantedTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionResult;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class MutandisItem extends Item {

    private static final Random RANDOM = new Random();
    private final TagKey<Block> validBlocks;

    public MutandisItem(TagKey<Block> validBlocks, Properties properties) {
        super(properties);
        this.validBlocks = validBlocks;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.MUTANDIS_PLANTS).stream().allMatch(block -> ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.MUTANDIS_BLACKLIST).contains(block)))
            return InteractionResult.FAIL; // If all whitelisted plants are blacklisted

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if(!ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.MUTANDIS_BLACKLIST).contains(state.getBlock()) && ForgeRegistries.BLOCKS.tags().getTag(validBlocks).contains(state.getBlock())) {
            Level world = context.getLevel();
            if(!world.isClientSide) {

                BlockState newState = null;
                while(newState == null) {
                    Block newBlock = ForgeRegistries.BLOCKS.tags().getTag(validBlocks).getRandomElement(RANDOM).get();
                    if(!ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.MUTANDIS_BLACKLIST).contains(newBlock)) {
                        newState = newBlock.defaultBlockState();
                    }
                }

                world.setBlockAndUpdate(context.getClickedPos(), newState);
                world.playSound(null, context.getClickedPos(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                if(!context.getPlayer().isCreative()) context.getItemInHand().shrink(1);
                return InteractionResult.CONSUME;
            }
            else {
                for(int i = 0; i < 10; i++) {
                    double dx = context.getClickedPos().getX() + Math.random();
                    double dy = context.getClickedPos().getY() + Math.random();
                    double dz = context.getClickedPos().getZ() + Math.random();
                    world.addParticle(ParticleTypes.WITCH, dx, dy, dz, 0.0D, 0.0D, 0.0D);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
