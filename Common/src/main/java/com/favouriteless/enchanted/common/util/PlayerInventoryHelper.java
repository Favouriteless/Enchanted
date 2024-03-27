package com.favouriteless.enchanted.common.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlayerInventoryHelper {

    /**
     * Attempts to put an item into the given player's inventory, spawns it on the floor if the item does not fit
     */
    public static void tryGiveItem(Player player, ItemStack item) {
        if(player != null && !player.getInventory().add(item))
            player.level.addFreshEntity(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), item));
    }

}
