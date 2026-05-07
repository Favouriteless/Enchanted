package net.favouriteless.enchanted.neoforge.common.capabilities;

import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public record KettleInvWrapper(KettleBlockEntity kettle, @Nullable Direction side) implements IItemHandler {

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return kettle.getResult();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack; // Insertion is not allowed
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return amount > 0 ? kettle.takeItem(ItemStack.EMPTY, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return Item.ABSOLUTE_MAX_STACK_SIZE;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return false; // Insertion is not allowed
    }

}
