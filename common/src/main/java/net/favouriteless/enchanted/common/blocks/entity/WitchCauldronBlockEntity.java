package net.favouriteless.enchanted.common.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WitchCauldronBlockEntity extends BlockEntity {

    public WitchCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntityTypes.WITCH_CAULDRON.get(), pos, state);
    }

}
