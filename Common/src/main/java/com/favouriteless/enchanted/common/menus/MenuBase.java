package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blocks.entity.ContainerBlockEntityBase;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;

public abstract class MenuBase<T extends ContainerBlockEntityBase> extends AbstractContainerMenu {

	protected final T blockEntity;
	protected final Block block;

	protected MenuBase(MenuType<?> type, int id, T blockEntity, Block block) {
		super(type, id);
		this.block = block;
		this.blockEntity = blockEntity;
	}

	@Override
	public boolean stillValid(Player player) {
		return blockEntity.stillValid(player);
	}

	/**
	 * Add slots to the menu for a player's full inventory, including hotbar.
	 * @param playerInventory The {@link Inventory} the slots are for.
	 * @param xStart The x position of the leftmost slots.
	 * @param yStart The y position of the topmost slots.
	 */
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

	public T getBlockEntity() {
		return blockEntity;
	}

}
