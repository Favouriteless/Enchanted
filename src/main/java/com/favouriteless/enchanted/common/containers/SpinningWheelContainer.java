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
import com.favouriteless.enchanted.common.tileentity.FurnaceTileEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;

public class SpinningWheelContainer extends FurnaceContainerBase {

	public SpinningWheelContainer(final int windowId, final PlayerInventory playerInventory, final FurnaceTileEntityBase tileEntity, final IIntArray furnaceDataIn) {
		super(EnchantedContainers.SPINNING_WHEEL.get(),
				windowId,
				tileEntity,
				IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
				4,
				furnaceDataIn);

		// Container Inventory
		this.addSlot(new SlotInput(tileEntity, 0, 45, 23)); // Main input
		this.addSlot(new SlotInput(tileEntity, 1, 33, 47)); // Ingredient input
		this.addSlot(new SlotInput(tileEntity, 2, 57, 47)); // Ingredient input
		this.addSlot(new SlotOutput(tileEntity, 3, 130, 35)); // Spinning wheel output

		this.AddInventorySlots(playerInventory);
	}

	public SpinningWheelContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
		this(windowId, playerInventory, getTileEntity(playerInventory, data), new IntArray(4));
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(canInteractWithCallable, player, EnchantedBlocks.SPINNING_WHEEL.get());
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
		return super.quickMoveStack(playerIn, index);
	}

}
