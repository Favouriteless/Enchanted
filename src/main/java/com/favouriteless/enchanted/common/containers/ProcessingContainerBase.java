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

import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.tileentity.InventoryTileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.Nullable;

public abstract class ProcessingContainerBase extends ContainerBase {

    protected final IIntArray data;

    protected ProcessingContainerBase(@Nullable ContainerType<?> type, int id, InventoryTileEntityBase tileEntity, IWorldPosCallable canInteractWithCallable, Block block, IIntArray data) {
        super(type, id, tileEntity, canInteractWithCallable, block);
        this.data = data;

        if(data != null) {
            addDataSlots(data);
        }
    }

    public IIntArray getData() {
        return data;
    }

    public class SlotFuel extends Slot {
        public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return (net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0);
        }
    }

    public class SlotInput extends Slot {
        public SlotInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() == EnchantedItems.CLAY_JAR.get());
        }
    }

    public class SlotOutput extends Slot {
        public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public class SlotJarInput extends Slot {
        public SlotJarInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return (stack.getItem() == EnchantedItems.CLAY_JAR.get());
        }

    }
}