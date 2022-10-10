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

import com.favouriteless.enchanted.common.tileentity.InventoryTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.Nullable;

public class ContainerBase extends Container {

	protected InventoryTileEntityBase tileEntity;
	protected final IWorldPosCallable canInteractWithCallable;
	protected final Block block;

	protected ContainerBase(@Nullable ContainerType<?> type, int id, InventoryTileEntityBase tileEntity, IWorldPosCallable canInteractWithCallable, Block block) {
		super(type, id);
		this.canInteractWithCallable = canInteractWithCallable;
		this.block = block;
		this.tileEntity = tileEntity;
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return stillValid(canInteractWithCallable, player, block);
	}

	protected void addInventorySlots(PlayerInventory playerInventory, int startX, int startY) {
		for (int y = 0; y < 3; y++) { // Main Inventory
			for (int x = 0; x < 9; x++) {
				addSlot(new Slot(playerInventory, x + (y * 9) + 9, startX + (x * 18), startY + (y * 18)));
			}
		}
		for (int x = 0; x < 9; x++) { // Hotbar
			addSlot(new Slot(playerInventory, x, 8 + (18 * x), startY + 58));
		}
	}

	protected static InventoryTileEntityBase getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data, Class<? extends InventoryTileEntityBase> type) {
		final TileEntity tileEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

		if(tileEntity != null && tileEntity.getClass() == type) {
			return (InventoryTileEntityBase)tileEntity;
		}
		throw new IllegalStateException("TileEntity at " + data.readBlockPos() + " is not correct");
	}

}
