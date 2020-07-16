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

public class WitchOvenContainer extends FurnaceContainerBase {

    public WitchOvenContainer(final int windowId, final PlayerInventory playerInventory, final FurnaceTileEntityBase tileEntity, final IIntArray furnaceDataIn) {
        super(MagicraftContainerTypes.WITCH_OVEN.get(),
                windowId,
                tileEntity,
                IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()),
                5,
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
        this.addSlot(new SlotInput(tileEntity, 0, 56, 17)); // Ingredient input
        this.addSlot(new SlotFuel(tileEntity, 1, 83, 53)); // Fuel Slot
        this.addSlot(new SlotOutput(tileEntity, 2, 110, 17)); // Smelting output
        this.addSlot(new SlotJarInput(tileEntity, 3, 56, 53)); // Jar input
        this.addSlot(new SlotOutput(tileEntity, 4, 110, 53)); // Jar output
    }

    public WitchOvenContainer(final int windowId, final PlayerInventory playerInventory, final PacketBuffer data) {
        this(windowId, playerInventory, getTileEntity(playerInventory, data), new IntArray(4));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return isWithinUsableDistance(canInteractWithCallable, player, MagicraftBlocks.WITCH_OVEN.get());
    }



}