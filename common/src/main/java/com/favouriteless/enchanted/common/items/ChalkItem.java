package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.blocks.chalk.AbstractChalkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ChalkItem extends BlockItem {

    private final AbstractChalkBlock chalkBlock;

    public ChalkItem(Block block, Properties properties) {
        super(block, properties);
        if(block instanceof AbstractChalkBlock) {
            chalkBlock = (AbstractChalkBlock) block;
        } else {
            throw new IllegalStateException("Chalk item registered with a non-chalk block.");
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getClickedFace() == Direction.UP) {
            Level level = context.getLevel();
            BlockPos clickedPos = context.getClickedPos();
            BlockState clickedState = level.getBlockState(clickedPos);

            if(clickedState.getBlock() instanceof AbstractChalkBlock) {
                tryPlaceChalk(level, clickedPos, context);
                return InteractionResult.SUCCESS;
            }
            else {
                BlockPos targetPos = clickedPos.above();
                BlockState targetState = level.getBlockState(targetPos);

                if(targetState.isAir() || targetState.getBlock() instanceof AbstractChalkBlock) {
                    if(chalkBlock.canSurvive(chalkBlock.defaultBlockState(), level, targetPos)) {
                        tryPlaceChalk(level, targetPos, context);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.FAIL;
    }

    private void tryPlaceChalk(Level level, BlockPos pos, UseOnContext context) {
        if(!level.isClientSide)
            level.setBlockAndUpdate(pos, chalkBlock.getStateForPlacement(new BlockPlaceContext(context)));
        level.playSound(context.getPlayer(), pos, EnchantedSoundEvents.CHALK_WRITE.get(), SoundSource.PLAYERS, 1F, 1F);
        context.getPlayer().getItemInHand(context.getHand()).hurtAndBreak(1, context.getPlayer(), (item) -> {});
    }



}
