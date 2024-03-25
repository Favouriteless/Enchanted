package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blocks.entity.DistilleryBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.common.menus.slots.JarInputSlot;
import com.favouriteless.enchanted.common.menus.slots.OutputSlot;
import com.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DistilleryMenu extends MenuBase<DistilleryBlockEntity> {

    public DistilleryMenu(int id, Inventory playerInventory, DistilleryBlockEntity be, ContainerData data) {
        super(EnchantedMenuTypes.DISTILLERY.get(), id, be, EnchantedBlocks.DISTILLERY.get());

        addSlot(new JarInputSlot(be, 0, 32, 35)); // Jar input
        addSlot(new Slot(be, 1, 54, 25)); // Ingredient input
        addSlot(new Slot(be, 2, 54, 45)); // Ingredient input

        addSlot(new OutputSlot(be, 3, 127, 7)); // Distillery output
        addSlot(new OutputSlot(be, 4, 127, 26)); // Distillery output
        addSlot(new OutputSlot(be, 5, 127, 45)); // Distillery output
        addSlot(new OutputSlot(be, 6, 127, 64)); // Distillery output

        addInventorySlots(playerInventory, 8, 84);
        addDataSlots(data);
    }

    public DistilleryMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, MenuUtils.getBlockEntity(playerInventory, data, DistilleryBlockEntity.class), new SimpleContainerData(2));
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack;
        Slot slot = slots.get(index);

        if(slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if(index < 7) { // If container slot
                if(!this.moveItemStackTo(slotItem, 7, 43, true))
                    return ItemStack.EMPTY;
            }
            else if(itemstack.getItem() == EnchantedItems.CLAY_JAR.get()) { // Item is clay jar
                if(!moveItemStackTo(slotItem, 0, 1, false))
                    return ItemStack.EMPTY;
            }
            else if(index < 34) { // Item is in main player inventory and cannot fit
                if(moveItemStackTo(slotItem, 1, 3, false) || !this.moveItemStackTo(slotItem, 34, 43, false))
                    return ItemStack.EMPTY;
            }
            else { // Item is in player hotbar and cannot fit
                if(moveItemStackTo(slotItem, 1, 3, false) || !this.moveItemStackTo(slotItem, 7, 34, false))
                    return ItemStack.EMPTY;
            }

            if(slotItem.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if(slotItem.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, slotItem);
        }
        return ItemStack.EMPTY;
    }

}