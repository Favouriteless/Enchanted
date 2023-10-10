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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MenuBase extends AbstractContainerMenu {

	protected BlockEntity blockEntity;
	protected final ContainerLevelAccess canInteractWithCallable;
	protected final Block block;

	protected MenuBase(MenuType<?> type, int id, BlockEntity blockEntity, ContainerLevelAccess canInteractWithCallable, Block block) {
		super(type, id);
		this.canInteractWithCallable = canInteractWithCallable;
		this.block = block;
		this.blockEntity = blockEntity;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(canInteractWithCallable, player, block);
	}

	protected void addInventorySlots(Inventory playerInventory, int startX, int startY) {
		for (int y = 0; y < 3; y++) { // Main Inventory
			for (int x = 0; x < 9; x++) {
				addSlot(new Slot(playerInventory, x + (y * 9) + 9, startX + (x * 18), startY + (y * 18)));
			}
		}
		for (int x = 0; x < 9; x++) { // Hotbar
			addSlot(new Slot(playerInventory, x, 8 + (18 * x), startY + 58));
		}
	}

	protected static BlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data, Class<? extends BlockEntity> type) {
		final BlockEntity blockEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

		if(blockEntity != null && blockEntity.getClass() == type) {
			return blockEntity;
		}
		throw new IllegalStateException("BlockEntity at " + data.readBlockPos() + " is not correct");
	}

}
