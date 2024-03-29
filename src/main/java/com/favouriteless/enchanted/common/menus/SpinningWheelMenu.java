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

import com.favouriteless.enchanted.common.blockentities.SpinningWheelBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenus;
import com.favouriteless.enchanted.common.menus.slots.SlotInput;
import com.favouriteless.enchanted.common.menus.slots.SlotOutput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SpinningWheelMenu extends MenuBase {

	private final ContainerData data;

	public SpinningWheelMenu(int id, Inventory playerInventory, SpinningWheelBlockEntity be, ContainerData data) {
		super(EnchantedMenus.SPINNING_WHEEL.get(), id, be, ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), EnchantedBlocks.SPINNING_WHEEL.get());
		this.data = data;

		addSlot(new SlotInput(be.getInputInventory(), 0, 45, 23)); // Main input
		addSlot(new SlotInput(be.getInputInventory(), 1, 33, 47)); // Ingredient input
		addSlot(new SlotInput(be.getInputInventory(), 2, 57, 47)); // Ingredient input
		addSlot(new SlotOutput(be.getOutputInventory(), 0, 130, 35)); // Spinning wheel output

		addInventorySlots(playerInventory, 8, 84);
		addDataSlots(data);
	}

	public SpinningWheelMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
		this(id, playerInventory, (SpinningWheelBlockEntity)getBlockEntity(playerInventory, data, SpinningWheelBlockEntity.class), new SimpleContainerData(2));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = slots.get(index);

		if (slot.hasItem()) {
			ItemStack slotItem = slot.getItem();
			ItemStack originalItem = slotItem.copy();

			if (index < 4) { // If container slot
				if (!moveItemStackTo(slotItem, 4, 40, true))
					return ItemStack.EMPTY;
			}
			else { // If in player inventory
				if(!moveItemStackTo(slotItem, 0, 4, false))
					return ItemStack.EMPTY;
			}

			if (slotItem.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();

			if (slotItem.getCount() == originalItem.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, slotItem);
		}
		return super.quickMoveStack(player, index);
	}

	public ContainerData getData() {
		return data;
	}

}
