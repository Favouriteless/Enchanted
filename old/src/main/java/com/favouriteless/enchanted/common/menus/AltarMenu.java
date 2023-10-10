/*
 *
 *   Copyright (c) 2023. Favouriteless
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

package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class AltarMenu extends AbstractContainerMenu {

    public final AltarBlockEntity blockEntity;
    private final ContainerLevelAccess canInteractWithCallable;
    private final ContainerData data;

    public AltarMenu(final int windowId, final AltarBlockEntity blockEntity, ContainerData data) {
        super(EnchantedMenus.ALTAR.get(), windowId);
        this.blockEntity = blockEntity;
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = data;
        addDataSlots(this.data);
    }

    public AltarMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, getBlockEntity(playerInventory, data), new SimpleContainerData(3));
    }

    private static AltarBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        final BlockEntity blockEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if(blockEntity instanceof AltarBlockEntity) {
            return (AltarBlockEntity)blockEntity;
        }
        throw new IllegalStateException("BlockEntity at " + data.readBlockPos() + " is not correct");
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
