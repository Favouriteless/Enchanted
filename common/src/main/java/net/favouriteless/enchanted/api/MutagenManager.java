package net.favouriteless.enchanted.api;

import net.favouriteless.enchanted.common.mutandis.MutagenManagerImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;

public interface MutagenManager {

    static MutagenManager get() {
        return MutagenManagerImpl.INSTANCE;
    }

    boolean tryStartMutating(ServerLevel level, BlockPos pos, boolean extremis);

    boolean isMutating(ServerLevel level, BlockPos pos);

    boolean canMutate(ServerLevel level, Block block);

}
