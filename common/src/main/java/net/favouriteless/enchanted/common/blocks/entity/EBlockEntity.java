package net.favouriteless.enchanted.common.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EBlockEntity extends BlockEntity {

    public EBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected void updateBlock() {
        if(level == null || level.isClientSide)
            return;
        BlockState state = level.getBlockState(worldPosition);
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_CLIENTS);
    }

    /**
     * Save for fields which should be sent to clients with {@link EBlockEntity#updateBlock()}
     */
    protected void saveSynced(CompoundTag tag, Provider registries) {
    }

    /**
     * Load for fields which should be sent to clients with {@link EBlockEntity#updateBlock()}
     */
    protected void loadSynced(CompoundTag tag, Provider registries) {
    }

    @Override
    protected void saveAdditional(CompoundTag tag, Provider registries) {
        saveSynced(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, Provider registries) {
        loadSynced(tag, registries);
    }

    @Override
    public CompoundTag getUpdateTag(Provider registries) {
        CompoundTag nbt = new CompoundTag();
        saveSynced(nbt, registries);
        return nbt;
    }

}
