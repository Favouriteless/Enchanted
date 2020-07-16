package com.favouriteless.magicraft.containers;

import com.favouriteless.magicraft.init.MagicraftItems;
import com.favouriteless.magicraft.tileentity.FurnaceTileEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class FurnaceContainerBase extends Container {

    public final FurnaceTileEntityBase tileEntity;
    protected final IWorldPosCallable canInteractWithCallable;
    protected final int numberOfSlots;
    protected final IIntArray furnaceData;

    protected FurnaceContainerBase(@Nullable ContainerType<?> type, int id, FurnaceTileEntityBase tileEntityIn, IWorldPosCallable canInteractWithCallableIn, int numberOfSlotsIn, IIntArray furnaceDataIn) {
        super(type, id);
        this.tileEntity = tileEntityIn;
        this.canInteractWithCallable = canInteractWithCallableIn;
        this.numberOfSlots = numberOfSlotsIn;
        this.furnaceData = furnaceDataIn;

        if(furnaceDataIn != null) {
            this.trackIntArray(furnaceDataIn);
        }
    }

    protected static FurnaceTileEntityBase getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "Player inventory cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");

        final TileEntity tileEntity = playerInventory.player.world.getTileEntity(data.readBlockPos());

        if(tileEntity instanceof FurnaceTileEntityBase) {
            return (FurnaceTileEntityBase)tileEntity;
        }
        throw new IllegalStateException("TileEntity at " + data.readBlockPos() + " is not correct");
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();
            if(index < this.numberOfSlots) {
                if(!this.mergeItemStack(itemStack1, this.numberOfSlots, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemStack1, 0, this.numberOfSlots, false)) {
                return ItemStack.EMPTY;
            }

            if(itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled(int size) {
        int cookTime = this.furnaceData.get(2);
        int cookTimeTotal = this.furnaceData.get(3);
        return cookTimeTotal != 0 && cookTime != 0 ? cookTime * size / cookTimeTotal : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int recipesUsed = this.furnaceData.get(1);
        int burnTime = this.furnaceData.get(0);
        if (recipesUsed == 0) {
            recipesUsed = 200;
        }

        return burnTime * 13 / recipesUsed;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.furnaceData.get(0) > 0;
    }



    public class SlotFuel extends Slot {
        public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack) {
            return (net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0);
        }
    }

    public class SlotInput extends Slot {
        public SlotInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack) {
            return !(stack.getItem() == MagicraftItems.CLAY_JAR.get());
        }
    }

    public class SlotOutput extends Slot {
        public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }

    public class SlotJarInput extends Slot {
        public SlotJarInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack) {
            return (stack.getItem() == MagicraftItems.CLAY_JAR.get());
        }

    }
}