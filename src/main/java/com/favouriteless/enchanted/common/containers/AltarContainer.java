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

import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedContainers;
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;

public class AltarContainer extends Container {

    public final AltarTileEntity tileEntity;
    private final IWorldPosCallable canInteractWithCallable;
    private final IIntArray data;

    public AltarContainer(final int windowId, final AltarTileEntity tileEntity, IIntArray data) {
        super(EnchantedContainers.ALTAR.get(), windowId);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos());
        this.data = data;
        addDataSlots(this.data);
    }

    public AltarContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, getTileEntity(playerInventory, data), new IntArray(3));
    }

    private static AltarTileEntity getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        final TileEntity tileEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if(tileEntity instanceof AltarTileEntity) {
            return (AltarTileEntity)tileEntity;
        }
        throw new IllegalStateException("TileEntity at " + data.readBlockPos() + " is not correct");
    }

    public int getCurrentPower() {
        return data.get(0);
    }

    public int getMaxPower() {
        return data.get(1);
    }

    public int getRechargeMultiplier() {
        return data.get(2);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(canInteractWithCallable, player, EnchantedBlocks.ALTAR.get());
    }
}
