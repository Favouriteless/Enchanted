package net.favouriteless.enchanted.common.menus;

import net.favouriteless.enchanted.common.blocks.entity.SpinningWheelBlockEntity;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EMenuTypes;
import net.favouriteless.enchanted.common.menus.slots.NonJarInputSlot;
import net.favouriteless.enchanted.common.menus.slots.OutputSlot;
import net.favouriteless.enchanted.common.util.MenuUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpinningWheelMenu extends ContainerMenuBase<SpinningWheelBlockEntity> {

    private final ContainerData data;

    public SpinningWheelMenu(int id, Inventory playerInventory, SpinningWheelBlockEntity be, ContainerData data) {
        super(EMenuTypes.SPINNING_WHEEL.get(), id, be, EBlocks.SPINNING_WHEEL.get());
        this.data = data;

        addSlot(new NonJarInputSlot(be, 0, 45, 23)); // Main input
        addSlot(new NonJarInputSlot(be, 1, 33, 47)); // Ingredient input
        addSlot(new NonJarInputSlot(be, 2, 57, 47)); // Ingredient input
        addSlot(new OutputSlot(be, 3, 130, 35)); // Spinning wheel output

        addInventorySlots(playerInventory, 8, 84);
        addDataSlots(data);
    }

    public SpinningWheelMenu(int id, Inventory playerInventory, BlockPos pos) {
        this(id, playerInventory, MenuUtils.getBlockEntity(playerInventory, pos, SpinningWheelBlockEntity.class), new SimpleContainerData(2));
    }

    public int getSpinProgress() {
        return data.get(0);
    }

    public int getSpinDuration() {
        return data.get(1);
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            ItemStack originalItem = slotItem.copy();

            if (index < 4) { // If container slot
                if (!moveItemStackTo(slotItem, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
            } else { // If in player inventory
                if (!moveItemStackTo(slotItem, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotItem.getCount() == originalItem.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotItem);
        }
        return ItemStack.EMPTY;
    }

}
