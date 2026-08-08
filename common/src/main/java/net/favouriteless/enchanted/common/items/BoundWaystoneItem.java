package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BoundWaystoneItem extends Item {

    public BoundWaystoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (stack.has(EDataComponents.BLOCK_POS.get())) {
            tooltip.add(Component.literal(stack.get(EDataComponents.BLOCK_POS.get()).toShortString()).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("item.enchanted.bound_waystone.not_bound").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId(stack)).withStyle(ChatFormatting.YELLOW);
    }

}