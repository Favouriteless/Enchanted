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

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.common.containers.PoppetShelfContainer;
import com.favouriteless.enchanted.common.init.EnchantedTileEntityTypes;
import com.favouriteless.enchanted.common.util.PoppetShelfManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PoppetShelfTileEntity extends InventoryTileEntityBase {

	public PoppetShelfTileEntity() {
		super(EnchantedTileEntityTypes.POPPET_SHELF.get(), NonNullList.withSize(4, ItemStack.EMPTY));
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.enchanted.poppet_shelf");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory playerInventory) {
		return new PoppetShelfContainer(id, playerInventory, this);
	}

	@Override
	protected void loadAdditional(CompoundNBT nbt) {

	}

	@Override
	protected void saveAdditional(CompoundNBT nbt) {

	}

	@Override
	public void setItem(int index, ItemStack stack) {
		if(!level.isClientSide) {
			PoppetShelfManager.removePoppet(inventoryContents.get(index), this);
			PoppetShelfManager.addPoppet(stack, this);
		}
		inventoryContents.set(index, stack);

		if (stack.getCount() > getMaxStackSize()) {
			stack.setCount(getMaxStackSize());
		}
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		PoppetShelfManager.removePoppet(inventoryContents.get(index), this);
		return super.removeItem(index, count);
	}



	@Override
	public void setItems(NonNullList<ItemStack> itemsIn) {
		inventoryContents = itemsIn;
	}
}
