/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.common.blocks.WitchCauldronBlock;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.witch_cauldron.WitchCauldronRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WitchCauldronTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

    private final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME*3, (fluid) -> fluid.getFluid() == Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    private NonNullList<ItemStack> inventoryContents = NonNullList.withSize(10, ItemStack.EMPTY);
    private final IItemHandlerModifiable items = new InvWrapper(this);
    private final LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    private static final int WARMING_MAX = 60;
    private int warmingUp = 0;
    private WitchCauldronRecipe currentRecipe;

    public WitchCauldronTileEntity() {
        super(EnchantedTileEntities.WITCH_CAULDRON.get());
    }

    @Override
    public void tick() {
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
        }
    }

    public void addItem(ItemEntity itemEntity) {
        if(itemEntity.isAlive()) {
            ItemStack itemIn = itemEntity.getItem();

            if (checkInventoryEmpty()) {
                tryAddNewItem(itemIn);
                itemEntity.kill();
            } else {
                for (int i = 0; i < inventoryContents.size(); i++) {
                    ItemStack stack = inventoryContents.get(i);
                    if (stack.sameItem(itemIn)) {
                        int transferAmount = Math.min((stack.getMaxStackSize() - stack.getCount()), itemIn.getCount());
                        stack.grow(transferAmount);
                        itemIn.shrink(transferAmount);

                        if (itemIn.isEmpty()) { // If item is fully added
                            itemEntity.kill();
                            break;
                        }
                    }
                    if (i == inventoryContents.size() - 1) { // If final item
                        if (tryAddNewItem(itemIn)) {
                            itemEntity.kill();
                        }
                        break;
                    }
                }
            }
        }
    }

    public boolean checkInventoryEmpty() {
        for(ItemStack stack : inventoryContents)
            if(stack != ItemStack.EMPTY) return false;
        return true;
    }

    public boolean tryAddNewItem(ItemStack itemIn) {
        for(int i = 0; i < inventoryContents.size(); i++) {
            if(inventoryContents.get(i) == ItemStack.EMPTY) {
                inventoryContents.set(i, itemIn);
                return true;
            }
        }
        return false;
    }

    private void clearItems() {
        inventoryContents.clear();
    }

    public boolean addWater(int amount) {
        if(tank.getFluidAmount() < tank.getCapacity()) {
            tank.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
            updateBlock();
            this.setChanged();
            return true;
        }
        return false;
    }

    public void setWater(int amount) {
        tank.setFluid(new FluidStack(Fluids.WATER, amount));
        updateBlock();
        this.setChanged();
    }

    private void updateBlock() {
        if(level != null && !level.isClientSide) {
            BlockState currentState = level.getBlockState(worldPosition);
            BlockState newState = EnchantedBlocks.WITCH_CAULDRON.get().defaultBlockState()
                    .setValue(WitchCauldronBlock.LEVEL, (int)Math.floor(tank.getFluidAmount() / 1000.0D))
                    .setValue(WitchCauldronBlock.HOT, warmingUp == WARMING_MAX);

            if(!currentState.equals(newState)) level.setBlockAndUpdate(worldPosition, newState);
        }
    }

    public boolean providesHeat(BlockState state){
        return  state.getBlock() == Blocks.FIRE ||
                state.getBlock() == Blocks.SOUL_FIRE ||
                state.getBlock() == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.SOUL_CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.MAGMA_BLOCK;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("waterAmount", tank.getFluidAmount());
        ItemStackHelper.saveAllItems(nbt, inventoryContents);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        setWater(nbt.getInt("waterAmount"));
        ItemStackHelper.loadAllItems(nbt, inventoryContents);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidHandler.cast();
        else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.witch_cauldron");
    }

    @Override
    protected Container createMenu(int pId, PlayerInventory pPlayer) {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventoryContents;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItems) {
        inventoryContents = pItems;
    }

    @Override
    public int getContainerSize() {
        return inventoryContents.size();
    }
}
