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

import com.favouriteless.enchanted.common.tileentity.KettleTileEntity;
import com.favouriteless.enchanted.core.util.PlayerInventoryHelper;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
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
import net.minecraftforge.fluids.FluidAttributes;

import javax.annotation.Nullable;

public class KettleBlock extends Block implements ITileEntityProvider {

    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);

    public KettleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        TileEntity te = world.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(hand);
        if(te instanceof KettleTileEntity) {
            KettleTileEntity kettle = (KettleTileEntity)te;

            if(stack.getItem() == Items.GLASS_BOTTLE && kettle.isComplete) {
                stack.shrink(1);
                kettle.takeContents(player);
                return ActionResultType.SUCCESS;
            }
            else if(stack.getItem() == Items.BUCKET && kettle.isFailed) {
                stack.shrink(1);
                kettle.takeContents(player);
                return ActionResultType.SUCCESS;
            }
            else if(stack.getItem() == Items.BUCKET && kettle.getWater() >= 1000) {
                if (!world.isClientSide) {
                    if (kettle.removeWater(FluidAttributes.BUCKET_VOLUME)) {
                        world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        stack.shrink(1);
                        PlayerInventoryHelper.tryGiveItem(player, new ItemStack(Items.WATER_BUCKET));
                    }
                }
                return ActionResultType.SUCCESS;
            }
            else if (stack.getItem() == Items.WATER_BUCKET) {
                if (!world.isClientSide) {
                    if (kettle.addWater(FluidAttributes.BUCKET_VOLUME)) {
                        world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        if (!player.isCreative()) player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new KettleTileEntity();
    }

    @Override
    public void entityInside(BlockState state, World world, BlockPos pos, Entity entity) {
        if(!world.isClientSide && entity instanceof ItemEntity) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof KettleTileEntity) {
                KettleTileEntity kettle = (KettleTileEntity)tileEntity;
                if(!kettle.isFailed && kettle.isFull() && kettle.isHot()) {
                    kettle.addItem((ItemEntity)entity);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return (pState.getValue(TYPE) == 1) ? VoxelShapes.box(0.1875, 0.125, 0.1875, 0.8125, 0.5, 0.8125) : VoxelShapes.box(0.1875, 0, 0.1875, 0.8125, 0.375, 0.8125);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean pIsMoving) {
        BlockState newState = getKettleState(world, pos, state.getValue(FACING));
        if(state != newState)
            world.setBlock(pos, newState, 2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return getKettleState(pContext.getLevel(), pContext.getClickedPos(), pContext.getHorizontalDirection().getOpposite());
    }

    public BlockState getKettleState(World world, BlockPos pos, @Nullable Direction facing) {
        if(facing == null) facing = Direction.NORTH;
        int type = 0;
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        if(world.getBlockState(pos.above()).isFaceSturdy(world, pos.above(), Direction.DOWN)) { // Face above is sturdy
            type = 1;
        }
        else if((world.getBlockState(pos.relative(left)).isFaceSturdy(world, pos.relative(left), right, BlockVoxelShape.CENTER)
                && world.getBlockState(pos.relative(right)).isFaceSturdy(world, pos.relative(right), left, BlockVoxelShape.CENTER))
                || (world.getBlockState(pos.relative(left)).getBlock() instanceof WallBlock && world.getBlockState(pos.relative(right)).getBlock() instanceof WallBlock)) { // Horizontal perpendicular to place direction is sturdy
            type = 2;
        }
        else if((world.getBlockState(pos.relative(facing)).isFaceSturdy(world, pos.relative(facing), facing.getOpposite(), BlockVoxelShape.CENTER)
                && world.getBlockState(pos.relative(facing.getOpposite())).isFaceSturdy(world, pos.relative(facing.getOpposite()), facing, BlockVoxelShape.CENTER))
                || (world.getBlockState(pos.relative(facing)).getBlock() instanceof WallBlock && world.getBlockState(pos.relative(facing.getOpposite())).getBlock() instanceof WallBlock)) { // Horizontal towards place direction is sturdy
            type = 2;
            facing = facing.getClockWise();
        }

        return defaultBlockState().setValue(FACING, facing).setValue(TYPE, type);
    }

}

