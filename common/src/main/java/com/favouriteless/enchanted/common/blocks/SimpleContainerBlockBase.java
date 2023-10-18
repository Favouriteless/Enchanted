package com.favouriteless.enchanted.common.blocks;

import com.favouriteless.enchanted.common.blockentities.ProcessingBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class SimpleContainerBlockBase extends BaseEntityBlock {

	public SimpleContainerBlockBase(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
		if(!level.isClientSide) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if(blockEntity instanceof MenuProvider) {
				NetworkHooks.openGui((ServerPlayer) player, (MenuProvider)blockEntity, pos);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity instanceof ProcessingBlockEntityBase) {
				Containers.dropContents(level, blockPos, ((ProcessingBlockEntityBase)blockEntity).getDroppableInventory());
			}
			super.onRemove(state, level, blockPos, newState, isMoving);
		}
	}

}
