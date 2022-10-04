/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TaglockFilledItem extends Item {

    public TaglockFilledItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World pLevel, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if(stack.hasTag()) {
            tooltip.add(new StringTextComponent(stack.getTag().getString("entityName")).withStyle(TextFormatting.GRAY));
        }
        super.appendHoverText(stack, pLevel, tooltip, flag);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return Rarity.EPIC;
    }

    @Override
    public boolean verifyTagAfterLoad(CompoundNBT nbt) {
        if (nbt.contains("entity") && nbt.contains("entityName")) {
            UUID id = NBTUtil.loadUUID(nbt.get("entity"));
            nbt.putUUID("entity", id);
            String entityName = nbt.getString("entityName");
            nbt.putString("entityName", entityName);
            return true;
        }
        return false;
    }

    public UUID getUUID(ItemStack stack) {
        if(stack.getItem() == EnchantedItems.TAGLOCK_FILLED.get() && stack.hasTag()) {
            return NBTUtil.loadUUID(stack.getTag().get("entity"));
        }
        return null;
    }

    @Override
    public int getUseDuration(ItemStack item) {
        return 32;
    }

    @Override
    public UseAction getUseAnimation(ItemStack item) {
        return UseAction.DRINK;
    }
}
