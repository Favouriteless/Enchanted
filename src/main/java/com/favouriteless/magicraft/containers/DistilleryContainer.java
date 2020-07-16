package com.favouriteless.magicraft.containers;

import com.favouriteless.magicraft.init.MagicraftBlocks;
import com.favouriteless.magicraft.init.MagicraftContainerTypes;
import com.favouriteless.magicraft.tileentity.FurnaceTileEntityBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;

public class DistilleryContainer extends FurnaceContainerBase {

    public DistilleryContainer(final int windowId, final PlayerInventory playerInventory, final FurnaceTileEntityBase tileEntity, final IIntArray furnaceDataIn) {
        super(MagicraftContainerTypes.DISTILLERY.get(),
                windowId,
                tileEntity,
                IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()),
                7,
                furnaceDataIn);

        // Player Inventory
        for (int x = 0; x < 9; x++) { // Hotbar
            int slotNumber = x;
            this.addSlot(new Slot(playerInventory, slotNumber, 8 + (18 * x), 142));
        }
        for (int y = 0; y < 3; y++) { // Main Inventory
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x + (y * 9) + 9,  8 + (x * 18), 84 + (y * 18)));
            }
        }

        // Container Inventory
        this.addSlot(new SlotJarInput(tileEntity, 0, 32, 35)); // Jar input
        this.addSlot(new SlotInput(tileEntity, 1, 54, 25)); // Ingredient input
        this.addSlot(new SlotInput(tileEntity, 2, 54, 45)); // Ingredient input
        this.addSlot(new SlotOutput(tileEntity, 3, 127, 7)); // Distillery output
        this.addSlot(new SlotOutput(tileEntity, 4, 127, 26)); // Distillery output
        this.addSlot(new SlotOutput(tileEntity, 5, 127, 45)); // Distillery output
        this.addSlot(new SlotOutput(tileEntity, 6, 127, 64)); // Distillery output
    }

    public DistilleryContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new IntArray(4));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return isWithinUsableDistance(canInteractWithCallable, player, MagicraftBlocks.DISTILLERY.get());
    }



}