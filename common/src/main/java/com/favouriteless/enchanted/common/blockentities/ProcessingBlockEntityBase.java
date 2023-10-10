package com.favouriteless.enchanted.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ProcessingBlockEntityBase extends BlockEntity {

    public ProcessingBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ContainerData getData();

    public static boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    protected int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            return ForgeHooks.getBurnTime(fuel, null);
        }
    }

    protected void updateBlock() {
        if(level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 2);
        }
    }

    public abstract NonNullList<ItemStack> getDroppableInventory();

    public NonNullList<ItemStack> getDroppableInventoryFor(IItemHandler... inventories) {
        NonNullList<ItemStack> drops = NonNullList.create();
        for(IItemHandler inventory : inventories)
            for(int i = 0; i < inventory.getSlots(); i++)
                drops.add(inventory.getStackInSlot(i));

        return drops;
    }

}