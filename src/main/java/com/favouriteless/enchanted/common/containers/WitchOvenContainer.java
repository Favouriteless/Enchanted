package com.favouriteless.enchanted.common.containers;

import com.favouriteless.enchanted.common.tileentity.FurnaceTileEntityBase;
import com.favouriteless.enchanted.core.init.EnchantedContainers;
import com.favouriteless.enchanted.core.init.EnchantedItems;
import com.favouriteless.enchanted.core.init.EnchantedRecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;

public class WitchOvenContainer extends FurnaceContainerBase {

    public WitchOvenContainer(final int windowId, final PlayerInventory playerInventory, final FurnaceTileEntityBase tileEntity, final IIntArray furnaceDataIn) {
        super(EnchantedContainers.WITCH_OVEN.get(),
                windowId,
                tileEntity,
                IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()),
                5,
                furnaceDataIn);

        // Container Inventory
        this.addSlot(new SlotInput(tileEntity, 0, 56, 17)); // Ingredient input
        this.addSlot(new SlotFuel(tileEntity, 1, 83, 53)); // Fuel Slot
        this.addSlot(new SlotOutput(tileEntity, 2, 110, 17)); // Smelting output
        this.addSlot(new SlotJarInput(tileEntity, 3, 56, 53)); // Jar input
        this.addSlot(new SlotOutput(tileEntity, 4, 110, 53)); // Jar output

        this.AddInventorySlots(playerInventory); // Player inventory starts at 5
    }

    public WitchOvenContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new IntArray(4));
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {

            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if (index <= 4) { // If container slot
                if (!this.moveItemStackTo(slotItem, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
            } else { // If not a container slot

                if (hasByproduct(slotItem)) { // Item has byproduct
                    if (!this.moveItemStackTo(slotItem, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (FurnaceTileEntityBase.isFuel(slotItem)) { // Item is fuel
                    if (!this.moveItemStackTo(slotItem, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if(itemstack.getItem() == EnchantedItems.CLAY_JAR.get()) { // Item is clay jar
                    if (!this.moveItemStackTo(slotItem, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 32) { // Item is in main player inventory or cannot be processed
                    if (!this.moveItemStackTo(slotItem, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 41) { // Item is in player hotbar or cannot be processed
                    if(!this.moveItemStackTo(slotItem, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.moveItemStackTo(slotItem, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }

                if (slotItem.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }

                if (slotItem.getCount() == itemstack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTake(playerIn, slotItem);
            }
        }
        return super.quickMoveStack(playerIn, index);
    }

    public boolean hasByproduct(ItemStack item) {
        if(this.tileEntity.getLevel() != null) {
            return this.tileEntity.getLevel().getRecipeManager().getRecipeFor(EnchantedRecipeTypes.WITCH_OVEN, new Inventory(item), this.tileEntity.getLevel()).isPresent();
        }
        return false;
    }
}