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
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfInventory;
import com.favouriteless.enchanted.common.util.poppet.PoppetShelfManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;

public class PoppetShelfTileEntity extends BlockEntity implements MenuProvider {

	public PoppetShelfInventory inventory = null;

	public PoppetShelfTileEntity() {
		super(EnchantedTileEntityTypes.POPPET_SHELF.get());
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("container.enchanted.poppet_shelf");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
		return new PoppetShelfContainer(id, playerInventory, this);
	}

	public void updateBlock() {
		if(level != null && !level.isClientSide) {
			BlockState state = level.getBlockState(worldPosition);
			level.sendBlockUpdated(worldPosition, state, state, 2);
		}
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		CompoundTag nbt = new CompoundTag();
		getInventory().save(nbt);
		return new ClientboundBlockEntityDataPacket(worldPosition, -1, nbt);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		getInventory().load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		getInventory().save(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag) {
		super.handleUpdateTag(state, tag);
		getInventory().load(tag);
	}

	public PoppetShelfInventory getInventory() {
		if(inventory == null) {
			if(level.isClientSide)
				inventory = new PoppetShelfInventory(level, worldPosition);
			else
				inventory = PoppetShelfManager.getInventoryFor(this);
		}
		return inventory;
	}

	@Override
	public void load(BlockState state, CompoundTag nbt) {
		super.load(state, nbt);
		setChanged();
	}
}
