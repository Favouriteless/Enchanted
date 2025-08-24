package net.favouriteless.enchanted.common.items;

import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BloodedWaystoneItem extends Item {

    public BloodedWaystoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if(stack.has(EDataComponents.ENTITY_REF.get()))
            tooltip.add(Component.literal(stack.get(EDataComponents.ENTITY_REF.get()).name()).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(getDescriptionId(stack)).withStyle(ChatFormatting.RED);
    }

}