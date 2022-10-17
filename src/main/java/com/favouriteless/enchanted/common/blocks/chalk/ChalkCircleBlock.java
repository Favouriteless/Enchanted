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

package com.favouriteless.enchanted.common.blocks.chalk;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ChalkCircleBlock extends AbstractChalkBlock {

    // Regular chalk types - white red purple
    public static final IntegerProperty GLYPH = IntegerProperty.create("glyph", 0, 47);
    public static final Random random = new Random();
    private final SimpleParticleType particleType;

    public ChalkCircleBlock(SimpleParticleType particleType) {
        super();
        this.particleType = particleType;
        this.registerDefaultState(getStateDefinition().any().setValue(GLYPH, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GLYPH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(GLYPH, random.nextInt(48));
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {

        if(this.particleType != null) {
            if(random.nextInt(6) == 1) {
                double dx = random.nextDouble();
                double dz = random.nextDouble();

                world.addParticle(particleType, pos.getX() + dx, pos.getY() + 0.1D, pos.getZ() + dz, 0, 0, 0);
            }
        }

    }

    public enum ChalkColour {
        WHITE,
        RED,
        PURPLE
    }
}