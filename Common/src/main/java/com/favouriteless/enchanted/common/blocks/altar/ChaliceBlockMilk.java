package com.favouriteless.enchanted.common.blocks.altar;

import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class ChaliceBlockMilk extends ChaliceBlock {

    public ChaliceBlockMilk(Properties properties) {
        super(properties, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if(!level.isClientSide) {
            player.removeAllEffects();
            level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);
            level.setBlockAndUpdate(pos, EnchantedBlocks.CHALICE.get().defaultBlockState());
        }
        return InteractionResult.SUCCESS;
    }

}
