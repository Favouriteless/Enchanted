/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;

public class RiteOfBindingTalisman extends AbstractCreateItemRite {

    protected RiteOfBindingTalisman(int power, int powerTick) {
        super(power, powerTick);
    }

    public RiteOfBindingTalisman() {
        super(1000, 0); // Power, power per tick
        ITEMS_REQUIRED.put(EnchantedItems.CIRCLE_TALISMAN.get(), 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
    }

    @Override
    public void execute() {
        for(ItemStack item : itemsConsumed) {
            if(item.getItem() == EnchantedItems.CIRCLE_TALISMAN.get() && item.hasTag()) {
                cancel();
                stopExecuting();
                return;
            }
        }
        byte small = testForCircle(CirclePart.SMALL);
        byte medium = testForCircle(CirclePart.MEDIUM);
        byte large = testForCircle(CirclePart.LARGE);

        if(small == 0 && medium == 0 && large == 0) {
            cancel();
            stopExecuting();
            return;
        }
        else {
            CompoundTag nbt = new CompoundTag();
            nbt.putByte("small", small);
            nbt.putByte("medium", medium);
            nbt.putByte("large", large);
            ItemStack talisman = new ItemStack(EnchantedItems.CIRCLE_TALISMAN.get(), 1, nbt);
            talisman.setTag(nbt);
            spawnItems(talisman);

            if(small != 0) CirclePart.SMALL.destroy(world, pos);
            if(medium != 0) CirclePart.MEDIUM.destroy(world, pos);
            if(large != 0) CirclePart.LARGE.destroy(world, pos);
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }

        stopExecuting();
    }

    private byte testForCircle(CirclePart size) {
        if(size.match(world, pos, EnchantedBlocks.CHALK_WHITE.get())) return 1;
        if(size.match(world, pos, EnchantedBlocks.CHALK_RED.get())) return 2;
        if(size.match(world, pos, EnchantedBlocks.CHALK_PURPLE.get())) return 3;
        return 0;
    }

    @Override
    public void onTick() {
    }

    @Override
    public RiteType<?> getType() {
        return EnchantedRiteTypes.BINDING_TALISMAN.get();
    }

}
