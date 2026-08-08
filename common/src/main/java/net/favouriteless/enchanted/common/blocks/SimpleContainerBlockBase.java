package net.favouriteless.enchanted.common.blocks;

import net.favouriteless.enchanted.common.blocks.entity.ContainerBlockEntityBase;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class SimpleContainerBlockBase<B extends SimpleContainerBlockBase<?>> extends EBaseEntityBlock<B> {

    public SimpleContainerBlockBase(Function<Properties, B> factory, Properties properties) {
        super(factory, properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MenuProvider mp) {
                EServices.PLATFORM.openMenu((ServerPlayer) player, mp, pos, BlockPos.STREAM_CODEC);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(blockPos) instanceof ContainerBlockEntityBase be) {
                Containers.dropContents(level, blockPos, be.getDroppableInventory());
            }
            super.onRemove(state, level, blockPos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity livingEntity, ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            if (level.getBlockEntity(pos) instanceof ContainerBlockEntityBase be) {
                be.setCustomName(stack.getHoverName());
            }
        }
    }

}
