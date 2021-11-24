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

package com.favouriteless.enchanted.common.blocks.altar;


import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class ChaliceBlock extends Block {

    private final boolean isFilled;

    public ChaliceBlock(Properties properties, boolean filled) {
        super(properties);
        this.isFilled = filled;
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if(state.is(EnchantedBlocks.CHALICE.get())) {

            if(player.getItemInHand(hand).getItem() == EnchantedItems.REDSTONE_SOUP.get()) {
                if (!world.isClientSide) {
                    world.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundCategory.BLOCKS, 0.4F, 1.0F);
                    world.setBlockAndUpdate(pos, EnchantedBlocks.CHALICE_FILLED.get().defaultBlockState());
                    player.getItemInHand(hand).shrink(1);
                    return ActionResultType.CONSUME;
                }
                return ActionResultType.SUCCESS;
            }

            else if(player.getItemInHand(hand).getItem() == Items.MILK_BUCKET) {
                if (!world.isClientSide) {
                    world.playSound(null, pos, SoundEvents.FISHING_BOBBER_SPLASH, SoundCategory.BLOCKS, 0.4F, 1.0F);
                    world.setBlockAndUpdate(pos, EnchantedBlocks.CHALICE_FILLED_MILK.get().defaultBlockState());
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET, 1));
                    return ActionResultType.CONSUME;
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.box(0.34375, 0, 0.34375, 0.65625, 0.4375, 0.65625);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if(isFilled) {
            double x = pos.getX() + 0.4D + random.nextDouble() * 0.2D;
            double y = pos.getY() + 0.5D;
            double z = pos.getZ() + 0.4D + random.nextDouble() * 0.2D;
            world.addParticle(new RedstoneParticleData(3.6F, 0.2F, 0.0F, 0.6F), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
