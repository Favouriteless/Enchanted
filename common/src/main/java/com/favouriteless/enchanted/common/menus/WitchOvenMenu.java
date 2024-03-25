package com.favouriteless.enchanted.common.menus;

import com.favouriteless.enchanted.common.blocks.entity.WitchOvenBlockEntity;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.common.menus.slots.FuelSlot;
import com.favouriteless.enchanted.common.menus.slots.JarInputSlot;
import com.favouriteless.enchanted.common.menus.slots.NonJarInputSlot;
import com.favouriteless.enchanted.common.menus.slots.OutputSlot;
import com.favouriteless.enchanted.common.util.ItemStackHelper;
import com.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WitchOvenMenu extends MenuBase<WitchOvenBlockEntity> {

    private final ContainerData data;

    public WitchOvenMenu(int id, Inventory playerInventory, WitchOvenBlockEntity be, ContainerData data) {
        super(EnchantedMenuTypes.WITCH_OVEN.get(), id, be, EnchantedBlocks.WITCH_OVEN.get());
        this.data = data;

        addSlot(new NonJarInputSlot(be, 0, 53, 17)); // Ingredient input
        addSlot(new FuelSlot(be, 0, 80, 53)); // Fuel Slot
        addSlot(new OutputSlot(be, 0, 107, 17)); // Smelting output
        addSlot(new JarInputSlot(be, 0, 53, 53)); // Jar input
        addSlot(new OutputSlot(be, 1, 107, 53)); // Jar output

        addInventorySlots(playerInventory, 8, 84);
        addDataSlots(data);
    }

    public WitchOvenMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, MenuUtils.getBlockEntity(playerInventory, data, WitchOvenBlockEntity.class), new SimpleContainerData(4));
    }

    public int getBurnProgress() {
        return data.get(0);
    }

    public int getBurnDuration() {
        return data.get(1);
    }

    public int getCookProgress() {
        return data.get(2);
    }

    public int getCookDuration() {
        return data.get(3);
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
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
            else if(ItemStackHelper.isFuel(slotItem)) { // Item is fuel
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
        return ItemStack.EMPTY;
    }

    protected boolean hasRecipe(ItemStack item) {
        Level level = getBlockEntity().getLevel();
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(item), level).isPresent();
    }

}