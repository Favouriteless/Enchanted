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

import com.favouriteless.enchanted.common.tileentity.DistilleryTileEntity;
import com.favouriteless.enchanted.core.init.EnchantedTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
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

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

public class DistilleryBlock extends ContainerBlock {

    // unfinished models but functionality done
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    
    private static final VoxelShape SHAPE_NORTH  = Stream.of(Block.box(0, 0, 0, 16, 1, 5), Block.box(7, 14, 6, 9, 16, 10), Block.box(6, 12, 8, 10, 14, 12), Block.box(5, 9, 7, 11, 12, 13), Block.box(4, 5, 6, 12, 9, 14), Block.box(5, 4, 7, 11, 5, 13), Block.box(0, 1, 2, 1, 7, 3), Block.box(15, 1, 2, 16, 7, 3), Block.box(1, 8, 2, 15, 9, 3), Block.box(0, 7, 2, 2, 8, 3), Block.box(14, 7, 2, 16, 8, 3), Block.box(5, 1, 6, 7, 3, 14), Block.box(9, 1, 6, 11, 3, 14), Block.box(4, 0, 7, 12, 2, 9), Block.box(4, 0, 11, 12, 2, 13), Block.box(2, 1, 1, 5, 6, 4), Block.box(6, 1, 1, 11, 3, 4), Block.box(12, 1, 1, 14, 4, 3), Block.box(12.5, 4, 1.5, 13.5, 5, 2.5)).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
    public static final VoxelShape SHAPE_SOUTH = Stream.of(Block.box(0, 0, 11, 16, 1, 16), Block.box(7, 14, 6, 9, 16, 10), Block.box(6, 12, 4, 10, 14, 8), Block.box(5, 9, 3, 11, 12, 9), Block.box(4, 5, 2, 12, 9, 10), Block.box(5, 4, 3, 11, 5, 9), Block.box(5, 1, 12, 10, 3, 15), Block.box(11, 1, 12, 14, 6, 15), Block.box(15, 1, 13, 16, 7, 14), Block.box(0, 7, 13, 2, 8, 14), Block.box(14, 7, 13, 16, 8, 14), Block.box(1, 8, 13, 15, 9, 14), Block.box(0, 1, 13, 1, 7, 14), Block.box(9, 1, 2, 11, 3, 10), Block.box(5, 1, 2, 7, 3, 10), Block.box(4, 0, 7, 12, 2, 9), Block.box(4, 0, 3, 12, 2, 5), Block.box(2, 1, 13, 4, 4, 15), Block.box(2.5, 4, 13.5, 3.5, 5, 14.5)).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
    public static final VoxelShape SHAPE_WEST = Stream.of(Block.box(0, 0, 0, 5, 1, 16), Block.box(6, 14, 7, 10, 16, 9), Block.box(8, 12, 6, 12, 14, 10), Block.box(7, 9, 5, 13, 12, 11), Block.box(6, 5, 4, 14, 9, 12), Block.box(7, 4, 5, 13, 5, 11), Block.box(1, 1, 5, 4, 3, 10), Block.box(1, 1, 11, 4, 6, 14), Block.box(2, 1, 15, 3, 7, 16), Block.box(2, 7, 0, 3, 8, 2), Block.box(2, 7, 14, 3, 8, 16), Block.box(2, 8, 1, 3, 9, 15), Block.box(2, 1, 0, 3, 7, 1), Block.box(6, 1, 9, 14, 3, 11), Block.box(6, 1, 5, 14, 3, 7), Block.box(7, 0, 4, 9, 2, 12), Block.box(11, 0, 4, 13, 2, 12), Block.box(1, 1, 2, 3, 4, 4), Block.box(1.5, 4, 2.5, 2.5, 5, 3.5)).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
    public static final VoxelShape SHAPE_EAST = Stream.of(Block.box(11, 0, 0, 16, 1, 16), Block.box(6, 14, 7, 10, 16, 9), Block.box(4, 12, 6, 8, 14, 10), Block.box(3, 9, 5, 9, 12, 11), Block.box(2, 5, 4, 10, 9, 12), Block.box(3, 4, 5, 9, 5, 11), Block.box(12, 1, 6, 15, 3, 11), Block.box(12, 1, 2, 15, 6, 5), Block.box(13, 1, 0, 14, 7, 1), Block.box(13, 7, 14, 14, 8, 16), Block.box(13, 7, 0, 14, 8, 2), Block.box(13, 8, 1, 14, 9, 15), Block.box(13, 1, 15, 14, 7, 16), Block.box(2, 1, 5, 10, 3, 7), Block.box(2, 1, 9, 10, 3, 11), Block.box(7, 0, 4, 9, 2, 12), Block.box(3, 0, 4, 5, 2, 12), Block.box(13, 1, 12, 15, 4, 14), Block.box(13.5, 4, 12.5, 14.5, 5, 13.5)).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
    


    public DistilleryBlock(Block.Properties builder) {
        super(builder);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return EnchantedTileEntities.DISTILLERY.get().create();
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return new DistilleryTileEntity();
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if(!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(tileEntity instanceof DistilleryTileEntity) {
                NetworkHooks.openGui((ServerPlayerEntity)player, (DistilleryTileEntity)tileEntity, pos);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = world.getBlockEntity(blockPos);
            if (tileentity instanceof DistilleryTileEntity) {
                DistilleryTileEntity tileEntity = (DistilleryTileEntity)tileentity;
                InventoryHelper.dropContents(world, blockPos, tileEntity);
            }
            super.onRemove(state, world, blockPos, newState, isMoving);  // call it last, because it removes the TileEntity
        }
    }


    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

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

    @OnlyIn(Dist.CLIENT)
    public float getShadeBrightness(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 0.7F;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
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
    public BlockRenderType getRenderShape(BlockState iBlockState) {
        return BlockRenderType.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) { }


}