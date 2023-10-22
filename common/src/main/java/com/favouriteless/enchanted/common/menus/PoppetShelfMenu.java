package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blockentities.PoppetShelfBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.common.items.poppets.AbstractPoppetItem;
import com.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PoppetShelfMenu extends MenuBase<PoppetShelfBlockEntity> {

	public PoppetShelfMenu(int id, Inventory playerInventory, PoppetShelfBlockEntity blockEntity) {
		super(EnchantedMenuTypes.POPPET_SHELF.get(), id, blockEntity, ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), EnchantedBlocks.POPPET_SHELF.get());

		for(int i = 0; i < blockEntity.getInventory().getContainerSize(); i++)
			addSlot(new PoppetSlot(blockEntity.getInventory(), i, 47 + i*22, 18));

		addInventorySlots(playerInventory, 8, 49);
	}

	public PoppetShelfMenu(final int windowId, final Inventory playerInventory, final FriendlyByteBuf data) {
		this(windowId, playerInventory, MenuUtils.getBlockEntity(playerInventory, data, PoppetShelfBlockEntity.class));
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack originalItem;
		Slot slot = slots.get(index);

		if (slot.hasItem()) {
			ItemStack slotItem = slot.getItem();
			originalItem = slotItem.copy();

			if (index < 4) { // If container slot
				if (!moveItemStackTo(slotItem, 4, 40, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!moveItemStackTo(slotItem, 0, 4, false))
				return ItemStack.EMPTY;

			if (slotItem.isEmpty())
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();

			if (slotItem.getCount() == originalItem.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, slotItem);
		}
		return super.quickMoveStack(playerIn, index);
	}

	public static class PoppetSlot extends Slot {

		public PoppetSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack itemStack) {
			return itemStack.getItem() instanceof AbstractPoppetItem;
		}
	}

}
