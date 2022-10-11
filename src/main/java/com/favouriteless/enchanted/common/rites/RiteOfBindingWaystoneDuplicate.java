/*
 * Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.CirclePart;
import com.favouriteless.enchanted.common.util.RiteType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RiteOfBindingWaystoneDuplicate extends AbstractCreateItemRite {

    protected RiteOfBindingWaystoneDuplicate(int power, int powerTick) {
        super(power, powerTick);
    }

    public RiteOfBindingWaystoneDuplicate() {
        super(500, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.BOUND_WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
    }

    @Override
    public void execute() {
        ItemStack stoneItem = null;

        for(ItemStack stack : itemsConsumed) {
            if(stack.getItem() == EnchantedItems.BOUND_WAYSTONE.get()) {
                stoneItem = stack;
            }
        }

        if(stoneItem != null && stoneItem.hasTag()) {
            ItemStack newStone = stoneItem.copy();
            spawnItems(stoneItem, newStone);
        }
        else {
            cancel();
            return;
        }

        stopExecuting();
    }

    @Override
    public void onTick() {
    }

    @Override
    public RiteType<?> getType() {
        return EnchantedRiteTypes.BINDING_WAYSTONE_DUPLICATE.get();
    }

}
