package com.favouriteless.enchanted.common.containers;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.tileentity.FurnaceTileEntityBase;
import com.favouriteless.enchanted.core.init.EnchantedItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
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

    protected FurnaceContainerBase(@Nullable ContainerType<?> type, int windowId, FurnaceTileEntityBase tileEntityIn, IWorldPosCallable canInteractWithCallableIn, int numberOfSlotsIn, IIntArray furnaceDataIn) {
        super(type, windowId);
        this.tileEntity = tileEntityIn;
        this.canInteractWithCallable = canInteractWithCallableIn;
        this.numberOfSlots = numberOfSlotsIn;
        this.furnaceData = furnaceDataIn;

        if(furnaceDataIn != null) {
            this.addDataSlots(furnaceDataIn);
        }
    }

    protected static FurnaceTileEntityBase getTileEntity(final PlayerInventory playerInventory, final PacketBuffer data) {
        Objects.requireNonNull(playerInventory, "Player inventory cannot be null");
        Objects.requireNonNull(data, "Data cannot be null");

        final TileEntity tileEntity = playerInventory.player.level.getBlockEntity(data.readBlockPos());

        if(tileEntity instanceof FurnaceTileEntityBase) {
            return (FurnaceTileEntityBase)tileEntity;
        }
        throw new IllegalStateException("TileEntity at " + data.readBlockPos() + " is not correct");
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.tileEntity.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }

    protected void AddInventorySlots(PlayerInventory playerInventory) {
        for (int y = 0; y < 3; y++) { // Main Inventory
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x + (y * 9) + 9,  8 + (x * 18), 84 + (y * 18)));
            }
        }
        for (int x = 0; x < 9; x++) { // Hotbar
            this.addSlot(new Slot(playerInventory, x, 8 + (18 * x), 142));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled(int size) {
        int cookTime = this.furnaceData.get(2);
        int cookTimeTotal = this.furnaceData.get(3);
        return cookTimeTotal != 0 && cookTime != 0 ? cookTime * size / cookTimeTotal : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgression() {
        return this.furnaceData.get(2);
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
        public boolean mayPlace(ItemStack stack) {
            return (net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0);
        }
    }

    public class SlotInput extends Slot {
        public SlotInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() == EnchantedItems.CLAY_JAR.get());
        }
    }

    public class SlotOutput extends Slot {
        public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public class SlotJarInput extends Slot {
        public SlotJarInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return (stack.getItem() == EnchantedItems.CLAY_JAR.get());
        }

    }
}