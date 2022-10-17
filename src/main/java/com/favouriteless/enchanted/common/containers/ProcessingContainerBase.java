/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.containers;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.blockentities.InventoryBlockEntityBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;

import javax.annotation.Nullable;

public abstract class ProcessingContainerBase extends ContainerBase {

    protected final ContainerData data;

    protected ProcessingContainerBase(@Nullable MenuType<?> type, int id, InventoryBlockEntityBase tileEntity, ContainerLevelAccess canInteractWithCallable, Block block, ContainerData data) {
        super(type, id, tileEntity, canInteractWithCallable, block);
        this.data = data;

        if(data != null) {
            addDataSlots(data);
        }
    }

    public ContainerData getData() {
        return data;
    }

    public class SlotFuel extends Slot {
        public SlotFuel(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return (net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0);
        }
    }

    public class SlotInput extends Slot {
        public SlotInput(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() == EnchantedItems.CLAY_JAR.get());
        }
    }

    public class SlotOutput extends Slot {
        public SlotOutput(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public class SlotJarInput extends Slot {
        public SlotJarInput(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return (stack.getItem() == EnchantedItems.CLAY_JAR.get());
        }

    }
}