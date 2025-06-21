package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.blocks.chalk.AbstractChalkBlock;
import net.favouriteless.enchanted.common.init.ESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ChalkItem extends BlockItem {

    private final AbstractChalkBlock chalkBlock;

    public ChalkItem(AbstractChalkBlock block, Properties properties) {
        super(block, properties);
        chalkBlock = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getClickedFace() != Direction.UP)
            return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if(state.getBlock() instanceof AbstractChalkBlock) {
            tryPlaceChalk(level, pos, context);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        BlockPos targetPos = pos.above();
        BlockState targetState = level.getBlockState(targetPos);

        if(targetState.canBeReplaced() && chalkBlock.canSurvive(chalkBlock.defaultBlockState(), level, targetPos))
            tryPlaceChalk(level, targetPos, context);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void tryPlaceChalk(Level level, BlockPos pos, UseOnContext context) {
        if(!level.isClientSide) {
            level.setBlockAndUpdate(pos, chalkBlock.getRandomState());
        }
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        level.playSound(player, pos, ESoundEvents.CHALK_WRITE.value(), SoundSource.PLAYERS, 1, 1);
        player.getItemInHand(hand).hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
    }

}
