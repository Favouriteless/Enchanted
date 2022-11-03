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
import com.favouriteless.enchanted.common.items.TaglockFilledItem;
import com.favouriteless.enchanted.common.util.WaystoneHelper;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RiteOfBindingWaystonePlayer extends AbstractCreateItemRite {

    protected RiteOfBindingWaystonePlayer(RiteType<?> type, int power, int powerTick) {
        super(type, power, powerTick, SoundEvents.ENDER_DRAGON_GROWL);
    }

    public RiteOfBindingWaystonePlayer() {
        this(EnchantedRiteTypes.BINDING_WAYSTONE_PLAYER.get(), 500, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.SLIME_BALL, 1);
        ITEMS_REQUIRED.put(Items.SNOWBALL, 1);
        ITEMS_REQUIRED.put(EnchantedItems.TAGLOCK_FILLED.get(), 1);
    }

    @Override
    public void execute() {
        if(targetUUID != null) {
            spawnItems(WaystoneHelper.create(targetUUID));
            spawnMagicParticles();
        }
        else {
            cancel();
        }
        stopExecuting();
    }

    @Override
    protected boolean checkAdditional() {
        for(ItemStack stack : itemsConsumed) {
            if(stack.getItem() == EnchantedItems.TAGLOCK_FILLED.get()) {
                targetUUID = TaglockFilledItem.getUUID(stack);
                if(targetUUID == null)
                    return false;
            }
        }
        return true;
    }

}
