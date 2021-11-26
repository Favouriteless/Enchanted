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

package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.tileentity.WitchOvenTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;
import java.util.stream.Stream;

public class WitchOvenBlock extends ContainerBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final VoxelShape SHAPE_NORTH = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(14, 0, 1, 15, 2, 2), Block.box(14, 2, 1, 15, 3, 15), Block.box(1, 2, 1, 2, 3, 15), Block.box(1, 0, 1, 2, 2, 2), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 0, 14, 2, 2, 15), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(6, 10, 9, 10, 16, 13), Block.box(5, 10, 8, 11, 12, 14)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(1, 0, 14, 2, 2, 15), Block.box(1, 2, 1, 2, 3, 15), Block.box(14, 2, 1, 15, 3, 15), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 0, 1, 2, 2, 2), Block.box(14, 0, 1, 15, 2, 2), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(6, 10, 3, 10, 16, 7), Block.box(5, 10, 2, 11, 12, 8)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(1, 0, 1, 2, 2, 2), Block.box(1, 2, 1, 15, 3, 2), Block.box(1, 2, 14, 15, 3, 15), Block.box(1, 0, 14, 2, 2, 15), Block.box(14, 0, 1, 15, 2, 2), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(9, 10, 6, 13, 16, 10), Block.box(8, 10, 5, 14, 12, 11)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(Block.box(2, 2, 2, 14, 3, 14), Block.box(14, 0, 14, 15, 2, 15), Block.box(1, 2, 14, 15, 3, 15), Block.box(1, 2, 1, 15, 3, 2), Block.box(14, 0, 1, 15, 2, 2), Block.box(1, 0, 14, 2, 2, 15), Block.box(1, 0, 1, 2, 2, 2), Block.box(1, 9, 1, 15, 10, 15), Block.box(2, 3, 2, 14, 9, 14), Block.box(3, 10, 3, 13, 11, 13), Block.box(3, 10, 6, 7, 16, 10), Block.box(2, 10, 5, 8, 12, 11)).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();


    public WitchOvenBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isClientSide()) {
            return ActionResultType.SUCCESS;
        } else {
            this.openContainer(world, pos, player);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader reader) {
        return new WitchOvenTileEntity();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof WitchOvenTileEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider)tileEntity, tileEntity.getBlockPos());
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean p_196243_5_) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof WitchOvenTileEntity) {
                InventoryHelper.dropContents(world, pos, (WitchOvenTileEntity)tileentity);
            }
            super.onRemove(state, world, pos, newState, p_196243_5_);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(LIT)) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY();
            double d2 = (double) pos.getZ() + 0.5D;
            if (rand.nextDouble() < 0.1D) {
                world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
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

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.7f;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
