package net.favouriteless.enchanted.common.blocks;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.favouriteless.enchanted.common.poppet.PoppetShelfManager;
import net.favouriteless.enchanted.platform.EServices;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PoppetShelfBlock extends BaseEntityBlock {

	public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

	private final MapCodec<PoppetShelfBlock> codec = simpleCodec(PoppetShelfBlock::new);

	public PoppetShelfBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PoppetShelfBlockEntity(pos, state);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return codec;
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
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if(!level.isClientSide) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if(blockEntity instanceof PoppetShelfBlockEntity be)
				EServices.PLATFORM.openMenu((ServerPlayer)player, be, pos, BlockPos.STREAM_CODEC);
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
					ItemUtils.dropContentsNoChange(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), shelf.getInventory());
				PoppetShelfManager.removeShelf(shelf);
			}
			super.onRemove(state, world, blockPos, newState, isMoving);
		}
	}

}
