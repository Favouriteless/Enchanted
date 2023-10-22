package com.favouriteless.enchanted.common.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class MenuBase<T extends BlockEntity> extends AbstractContainerMenu {

	protected T blockEntity;
	protected final ContainerLevelAccess canInteractWithCallable;
	protected final Block block;

	protected MenuBase(MenuType<?> type, int id, T blockEntity, ContainerLevelAccess canInteractWithCallable, Block block) {
		super(type, id);
		this.canInteractWithCallable = canInteractWithCallable;
		this.block = block;
		this.blockEntity = blockEntity;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(canInteractWithCallable, player, block);
	}

	protected void addInventorySlots(Inventory playerInventory, int xStart, int yStart) {
		for (int y = 0; y < 3; y++) { // Main Inventory
			for (int x = 0; x < 9; x++) {
				addSlot(new Slot(playerInventory, x + (y * 9) + 9, xStart + (x * 18), yStart + (y * 18)));
			}
		}
		for (int x = 0; x < 9; x++) { // Hotbar
			addSlot(new Slot(playerInventory, x, 8 + (18 * x), yStart + 58));
		}
	}

}
