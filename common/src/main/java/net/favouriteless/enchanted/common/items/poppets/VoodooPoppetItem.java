package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.enchanted.poppet.PoppetHelper;
import net.favouriteless.enchanted.common.entities.VoodooItemEntity;
import net.favouriteless.enchanted.common.init.EDamageTypes;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.favouriteless.enchanted.common.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class VoodooPoppetItem extends PoppetItem {

    public VoodooPoppetItem(Properties properties) {
        super(null, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(PoppetHelper.isBound(stack) && entity instanceof ServerPlayer player) {
            EntityRefData data = PoppetHelper.getData(stack);
            ServerPlayer target = EntityUtils.tryFindPlayer(player.serverLevel(), data.uuid());
            if(target == null)
                return stack;

            ItemStack offhand = player.getOffhandItem();

            if(!PoppetHelper.tryUseVoodoo(player, target)) {
                stack.hurtAndBreak(stack.getMaxDamage(), player, EquipmentSlot.MAINHAND);
                return stack;
            }

            if(offhand.is(EItems.BONE_NEEDLE.get()) || offhand.is(EItems.ICY_NEEDLE.get())) {
                target.hurt(EDamageTypes.source(player.serverLevel(), EDamageTypes.VOODOO), 2.0F);
                stack.hurtAndBreak(2, player, EquipmentSlot.MAINHAND);
                offhand.shrink(1);
                return stack;
            }

            target.addDeltaMovement(player.getLookAngle().normalize().scale(1.0D));
            target.hurtMarked = true;
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getMainHandItem();
            if(PoppetHelper.isBound(stack)) {
                player.startUsingItem(hand);
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if(PoppetHelper.isBound(stack))
            tooltip.add(Component.literal(PoppetHelper.getData(stack).name()).withStyle(ChatFormatting.RED));
    }

    // Soft implements NeoForge's IItemExtension#hasCustomEntity
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    // Soft implements NeoForge's IItemExtension#createEntity
    public Entity createEntity(Level level, Entity item, ItemStack stack) {
        VoodooItemEntity voodoo = new VoodooItemEntity(EEntityTypes.VOODOO_ITEM.get(), level);
        voodoo.setPos(item.position());
        voodoo.setDeltaMovement(item.getDeltaMovement());
        voodoo.setItem(stack);
        voodoo.setPickUpDelay(40);
        if(item instanceof ItemEntity itemEntity && itemEntity.getOwner() != null)
            voodoo.setThrower(itemEntity.getOwner());
        return voodoo;
    }

}
