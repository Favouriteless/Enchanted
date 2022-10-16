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
import com.favouriteless.enchanted.common.tileentity.AltarBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;

public class AltarContainer extends AbstractContainerMenu {

    public final AltarBlockEntity tileEntity;
    private final ContainerLevelAccess canInteractWithCallable;
    private final ContainerData data;

    public AltarContainer(final int windowId, final AltarBlockEntity tileEntity, ContainerData data) {
        super(EnchantedContainers.ALTAR.get(), windowId);
        this.tileEntity = tileEntity;
        this.canInteractWithCallable = ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos());
        this.data = data;
        addDataSlots(this.data);
    }

    public AltarContainer(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, getTileEntity(playerInventory, data), new SimpleContainerData(3));
    }

    private static AltarBlockEntity getTileEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        final BlockEntity tileEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if(tileEntity instanceof AltarBlockEntity) {
            return (AltarBlockEntity)tileEntity;
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
    public boolean stillValid(Player player) {
        return stillValid(canInteractWithCallable, player, EnchantedBlocks.ALTAR.get());
    }
}
