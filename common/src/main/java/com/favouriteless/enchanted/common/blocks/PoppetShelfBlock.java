package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.blockentities.PoppetShelfBlockEntity;
import com.favouriteless.enchanted.common.poppet.PoppetShelfManager;
import com.favouriteless.enchanted.common.util.ItemStackHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PoppetShelfBlock extends BaseEntityBlock {

	public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

	public PoppetShelfBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PoppetShelfBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
		if(!worldIn.isClientSide) {
			BlockEntity blockEntity = worldIn.getBlockEntity(pos);
			if(blockEntity instanceof PoppetShelfBlockEntity) {
				NetworkHooks.openGui((ServerPlayer) player, (PoppetShelfBlockEntity)blockEntity, pos);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if(blockEntity instanceof PoppetShelfBlockEntity) {
				PoppetShelfBlockEntity shelf = (PoppetShelfBlockEntity) blockEntity;
				if(!world.isClientSide)
					ItemStackHelper.dropContentsNoChange(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), shelf.getInventory());
				PoppetShelfManager.removeShelf(shelf);
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}

}
