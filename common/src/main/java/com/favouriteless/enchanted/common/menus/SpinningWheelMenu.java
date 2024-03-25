package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blocks.entity.SpinningWheelBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.common.menus.slots.NonJarInputSlot;
import com.favouriteless.enchanted.common.menus.slots.OutputSlot;
import com.favouriteless.enchanted.common.menus.slots.SimpleDataSlot;
import com.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpinningWheelMenu extends MenuBase<SpinningWheelBlockEntity> {

	private final DataSlot data;

	public SpinningWheelMenu(int id, Inventory playerInventory, SpinningWheelBlockEntity be, DataSlot data) {
		super(EnchantedMenuTypes.SPINNING_WHEEL.get(), id, be, EnchantedBlocks.SPINNING_WHEEL.get());
		this.data = data;

		addSlot(new NonJarInputSlot(be, 0, 45, 23)); // Main input
		addSlot(new NonJarInputSlot(be, 1, 33, 47)); // Ingredient input
		addSlot(new NonJarInputSlot(be, 2, 57, 47)); // Ingredient input
		addSlot(new OutputSlot(be, 3, 130, 35)); // Spinning wheel output

		addInventorySlots(playerInventory, 8, 84);
		addDataSlot(data);
	}

	public SpinningWheelMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
		this(id, playerInventory, MenuUtils.getBlockEntity(playerInventory, data, SpinningWheelBlockEntity.class), new SimpleDataSlot());
	}

	public int getSpinProgress() {
		return data.get();
	}

	@Override
	@NotNull
	public ItemStack quickMoveStack(@NotNull Player player, int index) {
		Slot slot = slots.get(index);

		if (slot.hasItem()) {
			ItemStack slotItem = slot.getItem();
			ItemStack originalItem = slotItem.copy();

			if (index < 4) { // If container slot
				if (!moveItemStackTo(slotItem, 4, 40, true))
					return ItemStack.EMPTY;
			}
			else { // If in player inventory
				if(!moveItemStackTo(slotItem, 0, 4, false))
					return ItemStack.EMPTY;
			}

			if (slotItem.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();

			if (slotItem.getCount() == originalItem.getCount())
				return ItemStack.EMPTY;

			slot.onTake(player, slotItem);
		}
		return ItemStack.EMPTY;
	}

}
