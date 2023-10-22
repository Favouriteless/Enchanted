package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blockentities.ProcessingBlockEntityBase;
import com.favouriteless.enchanted.common.blockentities.WitchOvenBlockEntity;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.common.menus.slots.SlotFuel;
import com.favouriteless.enchanted.common.menus.slots.SlotInput;
import com.favouriteless.enchanted.common.menus.slots.SlotJarInput;
import com.favouriteless.enchanted.common.menus.slots.SlotOutput;
import com.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class WitchOvenMenu extends MenuBase<WitchOvenBlockEntity> {

    private final ContainerData data;

    public WitchOvenMenu(int id, Inventory playerInventory, WitchOvenBlockEntity be, ContainerData data) {
        super(EnchantedMenuTypes.WITCH_OVEN.get(), id, be, ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), EnchantedBlocks.WITCH_OVEN.get());
        this.data = data;

        addSlot(new SlotInput(be.getInputInventory(), 0, 53, 17)); // Ingredient input
        addSlot(new SlotFuel(be.getFuelInventory(), 0, 80, 53)); // Fuel Slot
        addSlot(new SlotOutput(be.getOutputInventory(), 0, 107, 17)); // Smelting output
        addSlot(new SlotJarInput(be.getJarInventory(), 0, 53, 53)); // Jar input
        addSlot(new SlotOutput(be.getOutputInventory(), 1, 107, 53)); // Jar output

        addInventorySlots(playerInventory, 8, 84);
        addDataSlots(data);
    }

    public WitchOvenMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, MenuUtils.getBlockEntity(playerInventory, data, WitchOvenBlockEntity.class), new SimpleContainerData(4));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            ItemStack originalItem = slotItem.copy();

            if (index <= 4) { // If slot from oven
                if (!moveItemStackTo(slotItem, 5, 41, true))
                    return ItemStack.EMPTY;
            } else if(hasRecipe(slotItem)) { // Item has viable recipe
                if(!moveItemStackTo(slotItem, 0, 1, false))
                    return ItemStack.EMPTY;
            }
            else if(ProcessingBlockEntityBase.isFuel(slotItem)) { // Item is fuel
                if(!moveItemStackTo(slotItem, 1, 2, false))
                    return ItemStack.EMPTY;
            }
            else if(originalItem.getItem() == EnchantedItems.CLAY_JAR.get()) { // Item is clay jar
                if(!moveItemStackTo(slotItem, 3, 4, false))
                    return ItemStack.EMPTY;
            }
            else if(index < 32) { // Item is in main player inventory but cannot be processed
                if(!moveItemStackTo(slotItem, 32, 41, false))
                    return ItemStack.EMPTY;
            }
            else if(index < 41) { // Item is in player hotbar but cannot be processed
                if(!moveItemStackTo(slotItem, 5, 32, false))
                    return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) // If slot was made empty, replace with empty stack
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (slotItem.getCount() == originalItem.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, slotItem);
        }
        return super.quickMoveStack(player, index);
    }

    public boolean hasRecipe(ItemStack item) {
        if(blockEntity.getLevel() != null)
            return !item.is(EnchantedTags.Items.ORES) &&
                    blockEntity.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(item), blockEntity.getLevel()).isPresent();
        return false;
    }

    public ContainerData getData() {
        return data;
    }

}