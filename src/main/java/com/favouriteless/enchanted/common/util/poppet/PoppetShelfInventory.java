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

package com.favouriteless.enchanted.common.util.poppet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.AbstractList;

public class PoppetShelfInventory extends AbstractList<ItemStack> implements IInventory {

	private final World level;
	private final BlockPos pos;
	private final String identifier;
	public NonNullList<ItemStack> inventoryContents = NonNullList.withSize(4, ItemStack.EMPTY);

	public PoppetShelfInventory(World level, BlockPos pos) {
		this.level = level;
		this.pos = pos;
		identifier = PoppetShelfWorldSavedData.getShelfIdentifier(level, pos);
	}

	@Override
	public int getContainerSize() {
		return size();
	}

	@Override
	public boolean isEmpty() {
		return isEmpty();
	}

	@Override
	public ItemStack getItem(int index) {
		return get(index);
	}

	@Override
	public ItemStack removeItem(int index, int count) {
		return ItemStackHelper.removeItem(this, index, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int index) {;
		return ItemStackHelper.takeItem(this, index);
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		set(index, itemStack);
	}

	@Override
	public void setChanged() {
		if(!level.isClientSide)
			PoppetShelfWorldSavedData.get(level).setDirty();
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return false;
	}

	@Override
	public void clearContent() {
		inventoryContents.clear();
	}

	public void save(CompoundNBT nbt) {
		ListNBT list = new ListNBT();
		for(ItemStack itemStack : inventoryContents) {
			CompoundNBT itemTag = new CompoundNBT();
			itemStack.save(itemTag);
			list.add(itemTag);
		}
		nbt.put("items", list);
	}

	public void load(CompoundNBT nbt) {
		ListNBT list = nbt.getList("items", 10);
		for(int i = 0; i < list.size(); i++) {
			CompoundNBT tag = (CompoundNBT)list.get(i);
			inventoryContents.set(i, ItemStack.of(tag));
		}
	}

	public World getLevel() {
		return level;
	}

	public BlockPos getPos() {
		return pos;
	}

	@Override
	public ItemStack get(int index) {
		return inventoryContents.get(index);
	}

	@Override
	public ItemStack set(int index, ItemStack value) {
		if(!level.isClientSide) {
			if(get(index).isEmpty()) {
				get(index).setCount(1);
			}
			PoppetShelfWorldSavedData data = PoppetShelfWorldSavedData.get(level);
			data.removePoppetUUID(identifier, get(index));
			data.setupPoppetUUID(identifier, value);
		}
		setChanged();
		return inventoryContents.set(index, value);
	}

	@Override
	public int size() {
		return inventoryContents.size();
	}
}
