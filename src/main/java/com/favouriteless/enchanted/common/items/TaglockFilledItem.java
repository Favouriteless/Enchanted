/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
            tooltip.add(new TextComponent(stack.getTag().getString("entityName")).withStyle(ChatFormatting.GRAY));
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
