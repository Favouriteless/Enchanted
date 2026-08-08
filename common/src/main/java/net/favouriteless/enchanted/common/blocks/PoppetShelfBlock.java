package net.favouriteless.enchanted.common.blocks;

import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.favouriteless.enchanted.common.enchanted.poppet.shelf.PoppetShelfManager;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PoppetShelfBlock extends EBaseEntityBlock<PoppetShelfBlock> {

    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public PoppetShelfBlock(Properties properties) {
        super(PoppetShelfBlock::new, properties);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof PoppetShelfBlockEntity be) {
            EServices.PLATFORM.openMenu((ServerPlayer) player, be, pos, BlockPos.STREAM_CODEC);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) {
            return;
        }

        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof PoppetShelfBlockEntity shelf) {
            ItemUtils.dropContentsNoChange(level, pos.getX(), pos.getY(), pos.getZ(), shelf.getInventory());
            PoppetShelfManager.get(serverLevel).remove(serverLevel, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

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

}
