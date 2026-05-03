package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetHelper;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.items.component.EntityRefData;
import net.favouriteless.enchanted.common.util.EntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class PoppetItem extends Item {

    private final PoppetColour colour;

    public PoppetItem(PoppetColour colour, Properties properties) {
        super(properties);
        this.colour = colour;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.fail(player.getItemInHand(hand));

        ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);

        boolean validMain = !PoppetHelper.isBound(main);
        boolean validOff = off.getItem() == EItems.TAGLOCK_FILLED.get() || off.has(EDataComponents.ENTITY_REF.get());

        if(!validMain || !validOff)
            return InteractionResultHolder.pass(main);

        if(level instanceof ServerLevel serverLevel) {
            EntityRefData data = off.get(EDataComponents.ENTITY_REF.get());
            Player target = EntityUtils.tryFindPlayer(serverLevel, data.uuid()); // Not NPE, already checked stack has component

            if(target != null) {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(main);
            }
        }

        return InteractionResultHolder.pass(main);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if(entity instanceof Player player) {
            ItemStack off = player.getOffhandItem();

            if(off.getItem() != EItems.TAGLOCK_FILLED.get() || !off.has(EDataComponents.ENTITY_REF.get()))
                return stack;

            UUID uuid = off.get(EDataComponents.ENTITY_REF.get()).uuid(); // Not NPE, already checked stack has component
            Player target = level.getPlayerByUUID(uuid);

            if(target != null) {
                PoppetHelper.bind(stack, target);
                off.consume(1, player);
            }
        }

        return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if(PoppetHelper.isBound(stack))
            tooltip.add(Component.literal(stack.get(EDataComponents.ENTITY_REF.get()).name()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Component getName(ItemStack stack) {
        if(PoppetHelper.isBound(stack))
            return Component.translatable(getDescriptionId(stack)).withStyle(ChatFormatting.YELLOW);
        return super.getName(stack);
    }

    public PoppetColour getColour() {
        return colour;
    }

}
