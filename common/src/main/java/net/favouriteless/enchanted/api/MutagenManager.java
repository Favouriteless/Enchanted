package net.favouriteless.enchanted.api;

import net.favouriteless.enchanted.common.mutandis.MutagenInfo.MutagenSet;
import net.favouriteless.enchanted.common.mutandis.MutagenManagerImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;

public interface MutagenManager {

    static MutagenManager get() {
        return MutagenManagerImpl.INSTANCE;
    }

    boolean tryStartMutating(ServerLevel level, BlockPos pos, boolean extremis);

    boolean isMutating(ServerLevel level, BlockPos pos);

    boolean canMutate(ServerLevel level, Block block);

    /**
     * @return A list containing all MutagenInfos containing result as a result.
     */
    Map<Block, List<MutagenSet>> getMutagensFor(Level level, Block result);

}
