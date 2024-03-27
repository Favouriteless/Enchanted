package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TaglockFilledItem extends Item {

    public TaglockFilledItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag flag) {
        if(stack.hasTag()) {
            tooltip.add(Component.literal(stack.getTag().getString("entityName")).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, pLevel, tooltip, flag);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return Rarity.EPIC;
    }

    public static UUID getUUID(ItemStack stack) {
        if(stack.getItem() == EnchantedItems.TAGLOCK_FILLED.get() && stack.hasTag()) {
            return NbtUtils.loadUUID(stack.getTag().get("entity"));
        }
        return null;
    }

    @Override
    public int getUseDuration(ItemStack item) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack item) {
        return UseAnim.DRINK;
    }
}
