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
import com.favouriteless.enchanted.common.tileentity.InventoryTileEntityBase;
import com.favouriteless.enchanted.common.tileentity.SpinningWheelTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;

public class SpinningWheelContainer extends ProcessingContainerBase {

	public SpinningWheelContainer(final int windowId, final PlayerInventory playerInventory, final InventoryTileEntityBase tileEntity, final IIntArray data) {
		super(EnchantedContainers.SPINNING_WHEEL.get(), windowId, tileEntity, IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), EnchantedBlocks.SPINNING_WHEEL.get(), data);

		// Container Inventory
		addSlot(new SlotInput(tileEntity, 0, 45, 23)); // Main input
		addSlot(new SlotInput(tileEntity, 1, 33, 47)); // Ingredient input
		addSlot(new SlotInput(tileEntity, 2, 57, 47)); // Ingredient input
		addSlot(new SlotOutput(tileEntity, 3, 130, 35)); // Spinning wheel output

		addInventorySlots(playerInventory, 8, 84);
	}

	public SpinningWheelContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, (InventoryTileEntityBase)getTileEntity(playerInventory, data, SpinningWheelTileEntity.class), new IntArray(2));
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		ItemStack itemStack;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasItem()) {

			ItemStack slotItem = slot.getItem();
			itemStack = slotItem.copy();

			if (index <= 3) { // If container slot
				if (!this.moveItemStackTo(slotItem, 4, 40, true)) {
					return ItemStack.EMPTY;
				}
			}
			else{ // If in player inventory
				if(!this.moveItemStackTo(slotItem, 0, 4, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotItem.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (slotItem.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, slotItem);
		}
		return super.quickMoveStack(playerIn, index);
	}

}
