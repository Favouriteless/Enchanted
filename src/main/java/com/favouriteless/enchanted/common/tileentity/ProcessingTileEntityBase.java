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

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;

public abstract class ProcessingTileEntityBase extends InventoryTileEntityBase implements ITickableTileEntity {

    public ProcessingTileEntityBase(TileEntityType<?> typeIn, NonNullList<ItemStack> inventoryContents) {
        super(typeIn, inventoryContents);
    }

    public abstract IIntArray getData();

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

    protected void updateBlock() {
        if(level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 2);
        }
    }

}