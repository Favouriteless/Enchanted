package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.init.EBlocks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ArtichokeSeedsItem extends ItemNameBlockItem {
    
    public ArtichokeSeedsItem(Properties properties) {
        super(EBlocks.WATER_ARTICHOKE.get(), properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockHitResult aboveHit = hitResult.withPosition(hitResult.getBlockPos().above());
        InteractionResult useResult = super.useOn(new UseOnContext(player, hand, aboveHit));
        return new InteractionResultHolder<>(useResult, player.getItemInHand(hand));
    }
}