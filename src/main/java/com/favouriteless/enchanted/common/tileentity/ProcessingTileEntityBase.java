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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public abstract class ProcessingTileEntityBase extends LockableLootTileEntity implements ITickableTileEntity {

    protected NonNullList<ItemStack> inventoryContents;
    protected IItemHandlerModifiable items = new InvWrapper(this);
    protected LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    protected int numPlayersUsing;

    public ProcessingTileEntityBase(TileEntityType<?> typeIn, NonNullList<ItemStack> inventoryContents) {
        super(typeIn);
        this.inventoryContents = inventoryContents;
    }

    public abstract IIntArray getData();

    @Override
    public int getContainerSize() {
        return inventoryContents.size();
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventoryContents;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        inventoryContents = itemsIn;
    }

    @Override
    protected abstract ITextComponent getDefaultName();

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        inventoryContents = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, inventoryContents);
        loadAdditional(nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        super.save(nbt);
        ItemStackHelper.saveAllItems(nbt, inventoryContents);
        saveAdditional(nbt);
        return nbt;
    }

    protected abstract void loadAdditional(CompoundNBT nbt);

    protected abstract void saveAdditional(CompoundNBT nbt);

    @Override
    public boolean triggerEvent(int id, int type) {
        if(id == 1) {
            numPlayersUsing = type;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(PlayerEntity player) {
        if(!player.isSpectator()) {
            if(numPlayersUsing < 0 ) {
                numPlayersUsing = 0;
            }
            numPlayersUsing++;
            onOpenOrClose();
        }
    }

    @Override
    public void stopOpen(PlayerEntity player) {
        if(!player.isSpectator()) {
            numPlayersUsing--;
            onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = getBlockState().getBlock();
        level.blockEvent(worldPosition, block, 1, numPlayersUsing);
        level.updateNeighborsAt(worldPosition, block);
    }

    @Override
    public void clearCache() {
        super.clearCache();
        if(itemHandler != null) {
            itemHandler.invalidate();
            itemHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if(itemHandler != null) {
            itemHandler.invalidate();
        }
    }

    public static boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(fuel, null);
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        inventoryContents.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
    }

    protected void updateBlock() {
        if(level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 2);
        }
    }
    
}