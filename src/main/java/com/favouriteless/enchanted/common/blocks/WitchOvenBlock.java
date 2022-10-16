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

package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.tileentity.WitchOvenTileEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class WitchOvenBlock extends SimpleContainerBlockBase<WitchOvenTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE_NORTH = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(14, 0, 1, 15, 2, 2), Block.box(14, 2, 1, 15, 3, 15), Block.box(1, 2, 1, 2, 3, 15), Block.box(1, 0, 1, 2, 2, 2), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 0, 14, 2, 2, 15), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(6, 10, 9, 10, 16, 13), Block.box(5, 10, 8, 11, 12, 14)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(1, 0, 14, 2, 2, 15), Block.box(1, 2, 1, 2, 3, 15), Block.box(14, 2, 1, 15, 3, 15), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 0, 1, 2, 2, 2), Block.box(14, 0, 1, 15, 2, 2), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(6, 10, 3, 10, 16, 7), Block.box(5, 10, 2, 11, 12, 8)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(1, 0, 1, 2, 2, 2), Block.box(1, 2, 1, 15, 3, 2), Block.box(1, 2, 14, 15, 3, 15), Block.box(1, 0, 14, 2, 2, 15), Block.box(14, 0, 1, 15, 2, 2), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(9, 10, 6, 13, 16, 10), Block.box(8, 10, 5, 14, 12, 11)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 2, 14, 15, 3, 15), Block.box(1, 2, 1, 15, 3, 2), Block.box(14, 0, 1, 15, 2, 2), Block.box(1, 0, 14, 2, 2, 15), Block.box(1, 0, 1, 2, 2, 2), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(3, 10, 6, 7, 16, 10), Block.box(2, 10, 5, 8, 12, 11)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public WitchOvenBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter reader) {
        return new WitchOvenTileEntity();
    }

    @Override
    public RenderShape getRenderShape(BlockState iBlockState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.49D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.49D : d4;
            world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch(state.getValue(FACING)) {
            default:
                return SHAPE_NORTH;
            case SOUTH:
                return SHAPE_SOUTH;
            case WEST:
                return SHAPE_WEST;
            case EAST:
                return SHAPE_EAST;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0.7f;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

}
