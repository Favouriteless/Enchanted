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

import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.witch_cauldron.WitchCauldronRecipe;
import com.google.gson.JsonArray;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WitchCauldronTileEntity extends LockableLootTileEntity implements ITickableTileEntity {

    private final FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME*3, (fluid) -> fluid.getFluid() == Fluids.WATER);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    private NonNullList<ItemStack> inventoryContents = NonNullList.create();
    private final IItemHandlerModifiable items = new InvWrapper(this);
    private final LazyOptional<IItemHandlerModifiable> itemHandler = LazyOptional.of(() -> items);

    private static final int WARMING_MAX = 80;
    private static final int COOK_TIME = 160;

    private List<WitchCauldronRecipe> potentialRecipes = new ArrayList<>();
    private int cookProgress = 0;
    private int warmingUp = 0;
    public boolean isFailed = false;
    public boolean isComplete = false;

    private boolean justLoaded = false;

    // Only needed client side
    private int renderColour = 0x3F76E4;


    public WitchCauldronTileEntity() {
        super(EnchantedTileEntities.WITCH_CAULDRON.get());
    }

    @Override
    public void tick() {
        if(level != null && !level.isClientSide) {
            if(justLoaded) {
                matchRecipes();
            }
            if (!isFailed && !isComplete) {

                BlockState stateBelow = level.getBlockState(worldPosition.below());

                if (providesHeat(stateBelow) && tank.getFluidAmount() == tank.getCapacity()) { // On top of heat block, cauldron is full
                    if (warmingUp < WARMING_MAX) {
                        warmingUp++;
                    } else if (potentialRecipes.size() == 1 && potentialRecipes.get(0).fullMatch(this, level)) { // Has final recipe
                        if (cookProgress < COOK_TIME) {
                            cookProgress++;
                        } else {
                            setComplete();
                        }
                    }
                } else {
                    if (cookProgress != 0)
                        setFailed(); // Fail if heat or water is lost while cooking
                    warmingUp = 0;
                }
            }
            updateBlock();
        }
    }

    private void setFailed() {
        resetValues();
        isFailed = true;
    }

    private void setComplete() {
        resetValues();
        isComplete = true;
    }

    public void takeContents(ItemStack stack) {
        if(level != null && !level.isClientSide) {
            ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX()+0.5D, worldPosition.getY()+1, worldPosition.getZ()+0.5D,
                    isFailed ? new ItemStack(Items.WATER_BUCKET) : potentialRecipes.get(0).getResultItem());
            level.addFreshEntity(itemEntity);
            level.playSound(null, worldPosition, SoundEvents.BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
            inventoryContents.clear();
            potentialRecipes.clear();
            resetValues();
            setWater(0);
            updateBlock();
        }
    }

    private void resetValues() {
        isFailed = false;
        isComplete = false;
        cookProgress = 0;
    }

    public void addItem(ItemEntity itemEntity) {
        if(itemEntity.isAlive()) {
            inventoryContents.add(itemEntity.getItem());
            itemEntity.kill();
            matchRecipes();
            if(potentialRecipes.isEmpty()) {
                setFailed();
            }
            updateBlock();
        }
    }

    public boolean addWater(int amount) {
        if(!isComplete && !isFailed) {
            if (tank.getFluidAmount() < tank.getCapacity()) {
                tank.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
                updateBlock();
                return true;
            }
        }
        return false;
    }

    public boolean removeWater(int amount) {
        if(!isComplete && !isFailed) {
            if (tank.getFluidAmount() >= amount) {
                tank.drain(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
                updateBlock();
                return true;
            }
        }
        return false;
    }

    public void setWater(int amount) {
        tank.setFluid(new FluidStack(Fluids.WATER, amount));
        updateBlock();
    }

    public int getWater() {
        return tank.getFluidAmount();
    }

    public int getRenderColour() {
        return renderColour;
    }

    public boolean isFull() {
        return tank.getFluidAmount() == tank.getCapacity();
    }

    private void matchRecipes() {
        if (level != null) {
            potentialRecipes = level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof WitchCauldronRecipe)
                    .map(recipe -> (WitchCauldronRecipe)recipe)
                    .filter(recipe -> recipe.matches(this, level))
                    .collect(Collectors.toList());
        }
    }

    public static boolean providesHeat(BlockState state){
        return  state.getBlock() == Blocks.FIRE ||
                state.getBlock() == Blocks.SOUL_FIRE ||
                state.getBlock() == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.SOUL_CAMPFIRE && state.getValue(CampfireBlock.LIT) ||
                state.getBlock() == Blocks.MAGMA_BLOCK;
    }

    private void recalculateRenderColour() {
        int invSize = inventoryContents.size();

        if(invSize == 0) {
            renderColour = 0x3F76E4;
        }
        else if(isComplete) {
            renderColour = 0xFF00CC;
        }
        else if(isFailed) {
            renderColour = 0x522609;
        }
        else if(cookProgress > 0) {
            renderColour = 0x6E0909;
        }
        else {
            renderColour = 0x2EFF2E;
        }
    }

    private void updateBlock() {
        recalculateRenderColour();
        if(level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 2);
        }
    }

    public boolean isHot() {
        return warmingUp == WARMING_MAX;
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putInt("waterAmount", tank.getFluidAmount());
        nbt.putInt("renderColour", renderColour);
        nbt.putBoolean("isFailed", isFailed);
        nbt.putBoolean("isComplete", isComplete);
        nbt.putInt("warmingUp", warmingUp);
        nbt.putInt("cookProgress", cookProgress);
        ItemStackHelper.saveAllItems(nbt, inventoryContents);
        return super.save(nbt);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        setWater(nbt.getInt("waterAmount"));
        renderColour = nbt.getInt("renderColour");
        isFailed = nbt.getBoolean("isFailed");
        isComplete = nbt.getBoolean("isComplete");
        warmingUp = nbt.getInt("warmingUp");
        cookProgress = nbt.getInt("cookProgress");

        // Have to load nbt weirdly because inventory is not a fixed size
        ListNBT listnbt = nbt.getList("Items", 10);
        for(int i = 0; i < listnbt.size(); ++i) {
            CompoundNBT compoundnbt = listnbt.getCompound(i);
            inventoryContents.add(ItemStack.of(compoundnbt));
        }

        justLoaded = true;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("waterAmount", tank.getFluidAmount());
        nbt.putInt("renderColour", renderColour);
        nbt.putBoolean("isFailed", isFailed);
        nbt.putBoolean("isComplete", isComplete);
        nbt.putInt("warmingUp", warmingUp);
        nbt.putInt("cookProgress", cookProgress);
        return new SUpdateTileEntityPacket(worldPosition, -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getTag();
        setWater(nbt.getInt("waterAmount"));
        renderColour = nbt.getInt("renderColour");
        isFailed = nbt.getBoolean("isFailed");
        isComplete = nbt.getBoolean("isComplete");
        warmingUp = nbt.getInt("warmingUp");
        cookProgress = nbt.getInt("cookProgress");
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
