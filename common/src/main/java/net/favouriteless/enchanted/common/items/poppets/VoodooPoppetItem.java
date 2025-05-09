package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.entities.VoodooItemEntity;
import net.favouriteless.enchanted.common.init.EnchantedDamageTypes;
import net.favouriteless.enchanted.common.init.registry.EEntityTypes;
import net.favouriteless.enchanted.common.init.registry.EItems;
import net.favouriteless.enchanted.common.poppet.PoppetUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class VoodooPoppetItem extends PoppetItem {

    public VoodooPoppetItem() {
        super(0.0F, 40, null);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(PoppetUtils.isBound(stack) && entity instanceof Player player) {
            ItemStack offHand = player.getOffhandItem();

            if(offHand.getItem() == EItems.BONE_NEEDLE.get() || offHand.getItem() == EItems.ICY_NEEDLE.get()) {
                if(PoppetUtils.isBound(stack)) {
                    if(level instanceof ServerLevel serverLevel) {
                        ServerPlayer target = PoppetUtils.getBoundPlayer(stack, serverLevel);
                        if(target != null && !target.isCreative() && PoppetUtils.tryVoodooPlayer(target, (ServerPlayer)player, stack)) {
                            target.hurt(EnchantedDamageTypes.source(level, EnchantedDamageTypes.VOODOO, player), 2.0F);
                            stack.hurtAndBreak(2, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                            offHand.shrink(1);
                        }
                    }
                }
            }
            else if(level instanceof ServerLevel serverLevel) {
                ServerPlayer target = PoppetUtils.getBoundPlayer(stack, serverLevel);
                if(target != null && !target.isCreative() && PoppetUtils.tryVoodooPlayer(target, (ServerPlayer)player, stack)) {
                    target.addDeltaMovement(player.getLookAngle().normalize().scale(1.0D));
                    target.hurtMarked = true;
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                }
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getMainHandItem();
            if(PoppetUtils.isBound(stack)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> toolTip, TooltipFlag flag) {
        if(PoppetUtils.isBound(stack))
            toolTip.add(Component.literal(PoppetUtils.getBoundName(stack)).withStyle(ChatFormatting.RED));
    }

    // This overrides an IForgeItem method.
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    // This overrides an IForgeItem method.
    public Entity createEntity(Level level, Entity item, ItemStack stack) {
        VoodooItemEntity voodoo = new VoodooItemEntity(EEntityTypes.VOODOO_ITEM.get(), level);
        voodoo.setPos(item.position());
        voodoo.setDeltaMovement(item.getDeltaMovement());
        voodoo.setItem(stack);
        voodoo.setPickUpDelay(40);
        if(item instanceof ItemEntity itemEntity && itemEntity.getOwner() != null)
            voodoo.setThrower(itemEntity.getOwner().getUUID());
        return voodoo;
    }

}
