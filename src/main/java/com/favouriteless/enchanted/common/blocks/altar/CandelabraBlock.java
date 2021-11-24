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

import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class CandelabraBlock extends AltarDecorationBlock {

    public CandelabraBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.box(0.15625, 0, 0.15625, 0.84375, 0.71875, 0.84375);
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        // North
        if(random.nextInt(10) > 7) {
            double x = pos.getX() + 0.475D + random.nextDouble() * 0.05D;
            double y = pos.getY() + 0.678D;
            double z = pos.getZ() + 0.225D + random.nextDouble() * 0.05D;
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
        // West
        if(random.nextInt(10) > 7) {
            double x1 = pos.getX() + 0.225D + random.nextDouble() * 0.05D;
            double y1 = pos.getY() + 0.678D;
            double z1 = pos.getZ() + 0.475D + random.nextDouble() * 0.05D;
            world.addParticle(ParticleTypes.SMOKE, x1, y1, z1, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x1, y1, z1, 0.0D, 0.0D, 0.0D);
        }
        // Center
        if(random.nextInt(10) > 7) {
            double x2 = pos.getX() + 0.475D + random.nextDouble() * 0.05D;
            double y2 = pos.getY() + 0.778D;
            double z2 = pos.getZ() + 0.475D + random.nextDouble() * 0.05D;
            world.addParticle(ParticleTypes.SMOKE, x2, y2, z2, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x2, y2, z2, 0.0D, 0.0D, 0.0D);
        }
        // East
        if(random.nextInt(10) > 7) {
            double x3 = pos.getX() + 0.725D + random.nextDouble() * 0.05D;
            double y3 = pos.getY() + 0.678D;
            double z3 = pos.getZ() + 0.475D + random.nextDouble() * 0.05D;
            world.addParticle(ParticleTypes.SMOKE, x3, y3, z3, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x3, y3, z3, 0.0D, 0.0D, 0.0D);
        }
        // South
        if(random.nextInt(10) > 7) {
            double x4 = pos.getX() + 0.475D + random.nextDouble() * 0.05D;
            double y4 = pos.getY() + 0.678D;
            double z4 = pos.getZ() + 0.725D + random.nextDouble() * 0.05D;
            world.addParticle(ParticleTypes.SMOKE, x4, y4, z4, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, x4, y4, z4, 0.0D, 0.0D, 0.0D);
        }
    }
}
