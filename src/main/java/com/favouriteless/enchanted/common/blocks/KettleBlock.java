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

package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.blockentities.CauldronBlockEntity;
import com.favouriteless.enchanted.common.blockentities.KettleBlockEntity;
import com.favouriteless.enchanted.core.util.PlayerInventoryHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidAttributes;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KettleBlock extends Block implements EntityBlock {

    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
    public static final VoxelShape TYPE_1_SHAPE = Shapes.box(0.1875, 0.125, 0.1875, 0.8125, 0.5, 0.8125);
    public static final VoxelShape TYPE_2_SHAPE = Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.375, 0.8125);

    public KettleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity te = world.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(hand);
        if(te instanceof KettleBlockEntity) {
            KettleBlockEntity kettle = (KettleBlockEntity)te;

            if(stack.getItem() == Items.GLASS_BOTTLE && kettle.isComplete) {
                stack.shrink(1);
                kettle.takeContents(player);
                return InteractionResult.SUCCESS;
            }
            else if(stack.getItem() == Items.BUCKET && kettle.isFailed) {
                kettle.takeContents(player);
                return InteractionResult.SUCCESS;
            }
            else if(stack.getItem() == Items.BUCKET && kettle.getWater() >= 1000) {
                if (!world.isClientSide) {
                    if (kettle.removeWater(FluidAttributes.BUCKET_VOLUME)) {
                        world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        stack.shrink(1);
                        PlayerInventoryHelper.tryGiveItem(player, new ItemStack(Items.WATER_BUCKET));
                    }
                }
                return InteractionResult.SUCCESS;
            }
            else if (stack.getItem() == Items.WATER_BUCKET) {
                if (!world.isClientSide) {
                    if (kettle.addWater(FluidAttributes.BUCKET_VOLUME)) {
                        world.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!player.isCreative()) player.setItemInHand(hand, Items.BUCKET.getDefaultInstance());
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KettleBlockEntity(pos, state);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if(!world.isClientSide && entity instanceof ItemEntity) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof KettleBlockEntity) {
                KettleBlockEntity kettle = (KettleBlockEntity)tileEntity;
                if(!kettle.isFailed && kettle.isFull() && kettle.isHot()) {
                    kettle.addItem((ItemEntity)entity);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return (pState.getValue(TYPE) == 1) ? TYPE_1_SHAPE : TYPE_2_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean pIsMoving) {
        BlockState newState = getKettleState(world, pos, state.getValue(FACING));
        if(state != newState)
            world.setBlock(pos, newState, 2);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return getKettleState(pContext.getLevel(), pContext.getClickedPos(), pContext.getHorizontalDirection().getOpposite());
    }

    public BlockState getKettleState(Level world, BlockPos pos, @Nullable Direction facing) {
        if(facing == null) facing = Direction.NORTH;
        int type = 0;
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();

        if(world.getBlockState(pos.above()).isFaceSturdy(world, pos.above(), Direction.DOWN)) { // Face above is sturdy
            type = 1;
        }
        else if((world.getBlockState(pos.relative(left)).isFaceSturdy(world, pos.relative(left), right, SupportType.CENTER)
                && world.getBlockState(pos.relative(right)).isFaceSturdy(world, pos.relative(right), left, SupportType.CENTER))
                || (world.getBlockState(pos.relative(left)).getBlock() instanceof WallBlock && world.getBlockState(pos.relative(right)).getBlock() instanceof WallBlock)) { // Horizontal perpendicular to place direction is sturdy
            type = 2;
        }
        else if((world.getBlockState(pos.relative(facing)).isFaceSturdy(world, pos.relative(facing), facing.getOpposite(), SupportType.CENTER)
                && world.getBlockState(pos.relative(facing.getOpposite())).isFaceSturdy(world, pos.relative(facing.getOpposite()), facing, SupportType.CENTER))
                || (world.getBlockState(pos.relative(facing)).getBlock() instanceof WallBlock && world.getBlockState(pos.relative(facing.getOpposite())).getBlock() instanceof WallBlock)) { // Horizontal towards place direction is sturdy
            type = 2;
            facing = facing.getClockWise();
        }

        return defaultBlockState().setValue(FACING, facing).setValue(TYPE, type);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == EnchantedBlockEntityTypes.KETTLE.get() ? CauldronBlockEntity::tick : null;
    }
}

