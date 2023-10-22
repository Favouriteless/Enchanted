package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;

public class AltarMenu extends AbstractContainerMenu {

    public final AltarBlockEntity blockEntity;
    private final ContainerLevelAccess canInteractWithCallable;
    private final ContainerData data;

    public AltarMenu(final int windowId, final AltarBlockEntity blockEntity, ContainerData data) {
        super(EnchantedMenuTypes.ALTAR.get(), windowId);
        this.blockEntity = blockEntity;
        this.canInteractWithCallable = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        this.data = data;
        addDataSlots(this.data);
    }

    public AltarMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
        this(windowId, MenuUtils.getBlockEntity(playerInventory, data, AltarBlockEntity.class), new SimpleContainerData(3));
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
