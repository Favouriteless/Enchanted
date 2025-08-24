package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;

import java.util.List;
import java.util.UUID;

public class TaglockFilledItem extends Item {

    public TaglockFilledItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if(stack.has(EDataComponents.ENTITY_REF.get()))
            tooltip.add(Component.literal(stack.get(EDataComponents.ENTITY_REF.get()).name()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId(stack)).withStyle(ChatFormatting.RED);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack item) {
        return UseAnim.DRINK;
    }

    public static UUID getUUID(ItemStack stack) {
        return stack.has(EDataComponents.ENTITY_REF.get()) ? stack.get(EDataComponents.ENTITY_REF.get()).uuid() : null;
    }

}
