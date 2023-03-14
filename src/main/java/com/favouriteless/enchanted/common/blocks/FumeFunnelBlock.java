/*
 *
 *   Copyright (c) 2023. Favouriteless
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

package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class FumeFunnelBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3 );

    private static final VoxelShape SHAPE = Shapes.box(1.0D/16, 0.0D, 1.0D/16, 15.0D/16, 14.0D/16, 15.0D/16);
    private static final VoxelShape SHAPE_TOP_NORTH = Shapes.box(5.0D/16, 0.0D, 8.0D/16, 11.0D/16, 8.0D/16, 14.0D/16);
    private static final VoxelShape SHAPE_TOP_SOUTH = Shapes.box(5.0D/16, 0.0D, 2.0D/16, 11.0D/16, 8.0D/16, 8.0D/16);
    private static final VoxelShape SHAPE_TOP_EAST = Shapes.box(2.0D/16, 0.0D, 5.0D/16, 8.0D/16, 8.0D/16, 11.0D/16);
    private static final VoxelShape SHAPE_TOP_WEST = Shapes.box(8.0D/16, 0.0D, 5.0D/16, 14.0D/16, 8.0D/16, 11.0D/16);

    public FumeFunnelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false).setValue(TYPE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.getValue(TYPE) == 1 ?
                switch(state.getValue(FACING)) {
            default -> SHAPE_TOP_NORTH;
            case SOUTH -> SHAPE_TOP_SOUTH;
            case EAST -> SHAPE_TOP_EAST;
            case WEST -> SHAPE_TOP_WEST;
        } : SHAPE;
    }

    public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 1.0F;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        return getWitchOvenState(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return getWitchOvenState(stateIn, (Level) worldIn, currentPos);
    }

    /**
     * Checks for adjacent witch oven and returns appropriate state
     * @return
     */
    public BlockState getWitchOvenState(BlockState state, Level world, BlockPos pos) {
        int type = 3;
        boolean lit = false;

        for(Direction dir : Direction.values()) {
            if(dir != Direction.UP) {
                BlockState ovenState = world.getBlockState(pos.offset(dir.getNormal()));
                if (ovenState.is(EnchantedBlocks.WITCH_OVEN.get())) {
                    if (dir != ovenState.getValue(WitchOvenBlock.FACING) && dir != ovenState.getValue(WitchOvenBlock.FACING).getOpposite()) {

                        state = state.setValue(FACING, ovenState.getValue(WitchOvenBlock.FACING));
                        lit = ovenState.getValue(WitchOvenBlock.LIT);

                        if (dir == Direction.DOWN) {
                            type = 1;
                        }
                        else if(dir == state.getValue(FACING).getClockWise()) {
                            type = 2;
                        }
                        else if(dir == state.getValue(FACING).getCounterClockWise()) {
                            type = 0;
                        }
                    }
                    break;
                }
            }
        }
        return state.setValue(TYPE, type).setValue(LIT, lit);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, TYPE);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state;
    }

    @Override
    public RenderShape getRenderShape(BlockState iBlockState) {
        return RenderShape.MODEL;
    }

     public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            if (state.getValue(TYPE) != 1) {
                if (rand.nextInt(8) == 0) {
                    double d0 = (double) pos.getX() + 0.5D;
                    double d1 = (double) pos.getY();
                    double d2 = (double) pos.getZ() + 0.5D;
                    if (rand.nextDouble() < 0.1D) {
                        world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                    }

                    Direction direction = state.getValue(FACING);
                    Direction.Axis direction$axis = direction.getAxis();
                    double d3 = 0.52D;
                    double d4 = rand.nextDouble() * 0.6D - 0.3D;
                    double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.49D : d4;
                    double d6 = rand.nextDouble() * 6.0D / 16.0D;
                    double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.49D : d4;
                    world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                    world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

}
