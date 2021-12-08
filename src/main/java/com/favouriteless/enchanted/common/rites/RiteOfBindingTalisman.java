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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.rites.util.CircleSize;
import com.favouriteless.enchanted.common.rites.util.RiteType;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class RiteOfBindingTalisman extends AbstractCreateItemRite {

    public RiteOfBindingTalisman() {
        super(1000, 0); // Power, power per tick
        ITEMS_REQUIRED.put(EnchantedItems.CIRCLE_TALISMAN.get(), 1);
    }

    @Override
    public void execute() {
        if(itemsConsumed.get(0).hasTag()) {
            cancel();
            stopExecuting();
            return;
        }
        byte small = testForCircle(CircleSize.SMALL);
        byte medium = testForCircle(CircleSize.MEDIUM);
        byte large = testForCircle(CircleSize.LARGE);

        if(small == -1 && medium == -1 && large == -1) {
            cancel();
            stopExecuting();
            return;
        }
        else {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putByte("small", small);
            nbt.putByte("medium", medium);
            nbt.putByte("large", large);
            ItemStack talisman = new ItemStack(EnchantedItems.CIRCLE_TALISMAN.get(), 1, nbt);
            talisman.setTag(nbt);
            spawnItem(talisman);

            if(small != -1) CircleSize.SMALL.destroy(world, pos);
            if(medium != -1) CircleSize.MEDIUM.destroy(world, pos);
            if(large != -1) CircleSize.LARGE.destroy(world, pos);
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        stopExecuting();
    }

    private byte testForCircle(CircleSize size) {
        if(size.match(world, pos, EnchantedBlocks.CHALK_WHITE.get())) return 0;
        if(size.match(world, pos, EnchantedBlocks.CHALK_RED.get())) return 1;
        if(size.match(world, pos, EnchantedBlocks.CHALK_PURPLE.get())) return 2;
        return -1;
    }

    @Override
    public void onTick() {
    }

    @Override
    public RiteType<?> getType() {
        return EnchantedRiteTypes.BINDING_TALISMAN.get();
    }

}
