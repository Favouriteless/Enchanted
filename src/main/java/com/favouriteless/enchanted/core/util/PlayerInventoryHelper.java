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

package com.favouriteless.enchanted.core.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerInventoryHelper {

    /**
     * Attempts to put an item into the given player's inventory, spawns it on the floor if the item does not fit
     * @param player
     * @param item
     */
    public static void tryGiveItem(Player player, ItemStack item) {
        if(player != null && !player.inventory.add(item)) {
            player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), item));
        }
    }

}
