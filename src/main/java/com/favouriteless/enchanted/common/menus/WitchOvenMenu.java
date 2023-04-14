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

package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blockentities.ProcessingBlockEntityBase;
import com.favouriteless.enchanted.common.blockentities.WitchOvenBlockEntity;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class WitchOvenMenu extends ProcessingMenuBase {

    public WitchOvenMenu(int id, Inventory playerInventory, WitchOvenBlockEntity blockEntity, ContainerData data) {
        super(EnchantedMenus.WITCH_OVEN.get(), id, blockEntity, ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), EnchantedBlocks.WITCH_OVEN.get(), data);
        ItemStackHandler inventory = blockEntity.getInventory();

        // SpinningWheelBlockEntity Inventory
        addSlot(new SlotInput(inventory, 0, 53, 17)); // Ingredient input
        addSlot(new SlotFuel(inventory, 1, 80, 53)); // Fuel Slot
        addSlot(new SlotOutput(inventory, 2, 107, 17)); // Smelting output
        addSlot(new SlotJarInput(inventory, 3, 53, 53)); // Jar input
        addSlot(new SlotOutput(inventory, 4, 107, 53)); // Jar output

        addInventorySlots(playerInventory, 8, 84);
    }

    public WitchOvenMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, (WitchOvenBlockEntity)getBlockEntity(playerInventory, data, WitchOvenBlockEntity.class), new SimpleContainerData(4));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            ItemStack originalItem = slotItem.copy();

            if (index <= 4) { // If slot from oven
                if (!moveItemStackTo(slotItem, 5, 41, true))
                    return ItemStack.EMPTY;
            } else if(hasRecipe(slotItem)) { // Item has viable recipe
                if(!moveItemStackTo(slotItem, 0, 1, false))
                    return ItemStack.EMPTY;
            }
            else if(ProcessingBlockEntityBase.isFuel(slotItem)) { // Item is fuel
                if(!moveItemStackTo(slotItem, 1, 2, false))
                    return ItemStack.EMPTY;
            }
            else if(originalItem.getItem() == EnchantedItems.CLAY_JAR.get()) { // Item is clay jar
                if(!moveItemStackTo(slotItem, 3, 4, false))
                    return ItemStack.EMPTY;
            }
            else if(index < 32) { // Item is in main player inventory but cannot be processed
                if(!moveItemStackTo(slotItem, 32, 41, false))
                    return ItemStack.EMPTY;
            }
            else if(index < 41) { // Item is in player hotbar but cannot be processed
                if(!moveItemStackTo(slotItem, 5, 32, false))
                    return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) // If slot was made empty, replace with empty stack
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (slotItem.getCount() == originalItem.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, slotItem);
        }
        return super.quickMoveStack(player, index);
    }

    public boolean hasRecipe(ItemStack item) {
        if(blockEntity.getLevel() != null)
            return !ForgeRegistries.ITEMS.tags().getTag(Tags.Items.ORES).contains(item.getItem()) &&
                    blockEntity.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(item), blockEntity.getLevel()).isPresent();
        return false;
    }

}