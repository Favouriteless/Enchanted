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

import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class PoppetShelfTileEntity extends LockableLootTileEntity {

	public PoppetShelfTileEntity() {
		super(EnchantedTileEntities.POPPET_SHELF.get());
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return null;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItems) {

	}

	@Override
	protected ITextComponent getDefaultName() {
		return null;
	}

	@Override
	protected Container createMenu(int pId, PlayerInventory pPlayer) {
		return null;
	}

	@Override
	public int getContainerSize() {
		return 0;
	}
}
