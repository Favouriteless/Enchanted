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
import com.favouriteless.enchanted.common.recipes.distillery.DistilleryRecipe;
import com.favouriteless.enchanted.common.recipes.witch_cauldron.WitchCauldronRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
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

    private static final int WARMING_MAX = 80;
    private static final int COOK_TIME = 160;

    private int warmingUp = 0;
    private int cookProgress = 0;
    private boolean isFailed;

    private ItemStack result = ItemStack.EMPTY;
    private WitchCauldronRecipe currentRecipe;

    public WitchCauldronTileEntity() {
        super(EnchantedTileEntities.WITCH_CAULDRON.get());
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide && !isFailed && result == ItemStack.EMPTY) { // Not failed and hasn't finished
            BlockState stateBelow = level.getBlockState(worldPosition.below());

            if(providesHeat(stateBelow)) {

                if (warmingUp < WARMING_MAX) { // Cauldron is still warming up
                    warmingUp = (tank.getFluidAmount() == tank.getCapacity()) ? warmingUp + 1 : 0;
                }
                if(currentRecipe != null) {
                    if (cookProgress < COOK_TIME) { // Cauldron is currently cooking
                        cookProgress++;
                    } else { // Cauldron has finished cooking
                        result = currentRecipe.getResultItem().copy();
                        inventoryContents.clear();
                    }
                }
            }
            else if(cookProgress != 0) {
                setFailed();
            }
            updateBlock();
        }
    }

    private void setFailed() {
        isFailed = true;
        cookProgress = 0;
        result = ItemStack.EMPTY;
    }

    public void takeContents(PlayerEntity player) {
        if(player != null && level != null && currentRecipe != null) {
            ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), result.copy());
            itemEntity.setNoPickUpDelay();
            level.addFreshEntity(itemEntity);
            resetValues();
        }
    }

    public void takeFailedContents(PlayerEntity player, ItemStack stack) {
        if(player != null && level != null) {
            resetValues();
            stack.shrink(1);
            ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), new ItemStack(Items.WATER_BUCKET, 1));
            itemEntity.setNoPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    private void resetValues() {
        inventoryContents.clear();
        setWater(0);
        isFailed = false;
        result = ItemStack.EMPTY;
        cookProgress = 0;
        warmingUp = 0;
        currentRecipe = null;
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
            matchRecipe();
        }
    }

    private boolean checkInventoryEmpty() {
        for(ItemStack stack : inventoryContents)
            if(stack != ItemStack.EMPTY) return false;
        return true;
    }

    private boolean tryAddNewItem(ItemStack itemIn) {
        for(int i = 0; i < inventoryContents.size(); i++) {
            if(inventoryContents.get(i) == ItemStack.EMPTY) {
                inventoryContents.set(i, itemIn);
                return true;
            }
        }
        return false;
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

            int cookState = 0;
            if(isFailed)
                cookState = 3;
            else if(result != ItemStack.EMPTY)
                cookState = 2;
            else if(cookProgress > 0)
                cookState = 1;


            BlockState newState = EnchantedBlocks.WITCH_CAULDRON.get().defaultBlockState()
                    .setValue(WitchCauldronBlock.LEVEL, (int)Math.floor(tank.getFluidAmount() / 1000.0D))
                    .setValue(WitchCauldronBlock.HOT, warmingUp == WARMING_MAX)
                    .setValue(WitchCauldronBlock.COOKSTATE, cookState);

            if(!currentState.equals(newState)) level.setBlockAndUpdate(worldPosition, newState);
        }
    }

    private void matchRecipe() {
        if (level != null) {
            WitchCauldronRecipe newRecipe = level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof WitchCauldronRecipe)
                    .map(recipe -> (WitchCauldronRecipe)recipe)
                    .filter(recipe -> recipe.matches(this, level))
                    .findFirst()
                    .orElse(null);

            if(newRecipe != currentRecipe) {
                currentRecipe = newRecipe;
                cookProgress = 0;
            }
        }
    }

    public static boolean providesHeat(BlockState state){
        return  state.getBlock() == Blocks.FIRE ||
                state.getBlock() == Blocks.SOUL_FIRE ||
                state.getBlock() == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.SOUL_CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.MAGMA_BLOCK;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("waterAmount", tank.getFluidAmount());
        nbt.putInt("warmingUp", warmingUp);
        ItemStackHelper.saveAllItems(nbt, inventoryContents);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        setWater(nbt.getInt("waterAmount"));
        warmingUp = nbt.getInt("warmingUp");
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
