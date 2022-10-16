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

package com.favouriteless.enchanted.common.containers;

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedContainers;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.tileentity.InventoryTileEntityBase;
import com.favouriteless.enchanted.common.tileentity.ProcessingTileEntityBase;
import com.favouriteless.enchanted.common.tileentity.WitchOvenTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;

public class WitchOvenContainer extends ProcessingContainerBase {

    public WitchOvenContainer(final int windowId, final Inventory playerInventory, final InventoryTileEntityBase tileEntity, final ContainerData data) {
        super(EnchantedContainers.WITCH_OVEN.get(), windowId, tileEntity, ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), EnchantedBlocks.WITCH_OVEN.get(), data);

        // Container Inventory
        this.addSlot(new SlotInput(tileEntity, 0, 53, 17)); // Ingredient input
        this.addSlot(new SlotFuel(tileEntity, 1, 80, 53)); // Fuel Slot
        this.addSlot(new SlotOutput(tileEntity, 2, 107, 17)); // Smelting output
        this.addSlot(new SlotJarInput(tileEntity, 3, 53, 53)); // Jar input
        this.addSlot(new SlotOutput(tileEntity, 4, 107, 53)); // Jar output

        this.addInventorySlots(playerInventory, 8, 84);
    }

    public WitchOvenContainer(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, playerInventory, (InventoryTileEntityBase)getTileEntity(playerInventory, data, WitchOvenTileEntity.class), new SimpleContainerData(4));
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {

            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if (index <= 4) { // If container slot
                if (!this.moveItemStackTo(slotItem, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else { // If not a container slot

                if (hasByproduct(slotItem)) { // Item has byproduct
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (ProcessingTileEntityBase.isFuel(slotItem)) { // Item is fuel
                    if (!this.moveItemStackTo(slotItem, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(itemstack.getItem() == EnchantedItems.CLAY_JAR.get()) { // Item is clay jar
                    if (!this.moveItemStackTo(slotItem, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 32) { // Item is in main player inventory or cannot be processed
                    if (!this.moveItemStackTo(slotItem, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 41) { // Item is in player hotbar or cannot be processed
                    if(!this.moveItemStackTo(slotItem, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(slotItem, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }

                if (slotItem.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }

                if (slotItem.getCount() == itemstack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTake(playerIn, slotItem);
            }
        }
        return super.quickMoveStack(playerIn, index);
    }

    public boolean hasByproduct(ItemStack item) {
        if(tileEntity.getLevel() != null) {
            return tileEntity.getLevel().getRecipeManager().getRecipeFor(EnchantedRecipeTypes.WITCH_OVEN, new SimpleContainer(item), this.tileEntity.getLevel()).isPresent();
        }
        return false;
    }
}