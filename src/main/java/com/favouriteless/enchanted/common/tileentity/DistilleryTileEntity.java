/*
 * Copyright (c) 2022. Favouriteless
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

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.containers.DistilleryContainer;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DistilleryTileEntity extends ProcessingTileEntityBase implements IAltarPowerConsumer {

    private DistilleryRecipe currentRecipe;
    private final List<BlockPos> potentialAltars = new ArrayList<>();

    private int burnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 200;
    private final IIntArray data = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return cookTime;
                case 1:
                    return cookTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    cookTime = value;
                case 1:
                    cookTimeTotal = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public DistilleryTileEntity(TileEntityType<?> typeIn) {
        super(typeIn, NonNullList.withSize(7, ItemStack.EMPTY));
    }

    public DistilleryTileEntity() {
        this(EnchantedTileEntities.DISTILLERY.get());
    }

    @Override
    protected void saveAdditional(CompoundNBT nbt) {
        AltarPowerHelper.savePosTag(potentialAltars, nbt);
        nbt.putInt("burnTime", burnTime);
        nbt.putInt("cookTime", cookTime);
        nbt.putInt("cookTimeTotal", cookTimeTotal);
    }

    @Override
    protected void loadAdditional(CompoundNBT nbt) {
        AltarPowerHelper.loadPosTag(potentialAltars, nbt);
        burnTime = nbt.getInt("burnTime");
        cookTime = nbt.getInt("cookTime");
        cookTimeTotal = nbt.getInt("cookTimeTotal");
    }

    @Override
    public void tick() {
        boolean isBurning = isBurning();
        boolean shouldSave = false;

        if (level != null && !level.isClientSide) {
            matchRecipe();
            AltarTileEntity altar = AltarPowerHelper.tryGetAltar(level, potentialAltars);

            if(canDistill(currentRecipe) && altar != null) {
                if(altar.currentPower > 10.0D) {
                    altar.currentPower -= 10.0D;
                    burnTime = 1;
                    cookTime++;


                    if(cookTime == cookTimeTotal) {
                        cookTime = 0;
                        distill(currentRecipe);
                    }
                }
            }
            else {
                burnTime = 0;
                cookTime = 0;
            }


            if (isBurning != isBurning()) {
                shouldSave = true;
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(AbstractFurnaceBlock.LIT, isBurning()), 3);
            }
        }

        if (shouldSave) {
            setChanged();
        }

    }

    protected void distill(DistilleryRecipe recipeIn) {
        if (recipeIn != null) {
            List<ItemStack> itemsOut = new ArrayList<>();

            for(ItemStack itemStack : recipeIn.getItemsOut()) {
                itemsOut.add(itemStack.copy());
            }

            for (ItemStack item : itemsOut) {
                for (int i = 3; i < 7; i++) { // Fit items into existing stacks
                    ItemStack stack = this.inventoryContents.get(i);
                    if (item.getItem() == stack.getItem()) {
                        int spaceLeft = stack.getMaxStackSize() - stack.getCount();
                        if (spaceLeft <= item.getCount()) {
                            stack.grow(spaceLeft);
                            item.shrink(spaceLeft);
                        }
                        else {
                            stack.grow(item.getCount());
                            item.shrink(item.getCount());
                            break;
                        }
                    }
                }
            }

            for(ItemStack item : itemsOut) {
                if (!item.isEmpty()) {
                    for (int i = 3; i < 7; i++) { // Fit items into empty stacks
                        if (this.inventoryContents.get(i).isEmpty()) {
                            this.inventoryContents.set(i, item.copy());
                            break;
                        }
                    }
                }
            }

            for(ItemStack item : recipeIn.getItemsIn()) {
                for (int i = 0; i < 3; i++) {
                    ItemStack stack = this.inventoryContents.get(i);
                    if(item.getItem() == stack.getItem()) {
                        stack.shrink(item.getCount());
                        break;
                    }
                }
            }

        }
    }

    protected boolean canDistill(@Nullable DistilleryRecipe recipeIn) {
        if(recipeIn != null) {
            List<ItemStack> itemsOut = new ArrayList<>(recipeIn.getItemsOut());
            List<ItemStack> outputSlots = new ArrayList<>(this.inventoryContents.subList(3, 7));

            List<ItemStack> toRemoveOut = new ArrayList<>();

            for(ItemStack item : itemsOut) { // Check for existing itemstacks of correct type and size.
                for(ItemStack stack : outputSlots) {
                    if(item.getItem() == stack.getItem()) {
                        if(item.getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                            toRemoveOut.add(item);
                            break;
                        }
                        else {
                            item.shrink(stack.getMaxStackSize() - stack.getCount());
                        }
                    }
                }
            }

            itemsOut.removeAll(toRemoveOut); // Itemstacks have been accounted for
            toRemoveOut.clear();

            boolean[] isEmpty = new boolean[] { outputSlots.get(0).isEmpty(),  outputSlots.get(1).isEmpty(),  outputSlots.get(2).isEmpty(),  outputSlots.get(3).isEmpty() };
            for(ItemStack item : itemsOut) { // Check for empty item slots
                for(int i = 0; i < isEmpty.length; i++) {
                    if(isEmpty[i]) {
                        toRemoveOut.add(item);
                        isEmpty[i] = false;
                        break;
                    }
                }
            }
            itemsOut.removeAll(toRemoveOut);


            return itemsOut.isEmpty();
        }
        return false;
    }

    private void matchRecipe() {
        if (level != null) {
            currentRecipe = level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof DistilleryRecipe)
                    .map(recipe -> (DistilleryRecipe) recipe)
                    .filter(recipe -> matchRecipe(recipe, getItemsIn()))
                    .findFirst()
                    .orElse(null);
            if(currentRecipe != null) {
                this.cookTimeTotal = this.currentRecipe.getCookTime();
            }
        }
    }

    private boolean matchRecipe(DistilleryRecipe recipe, List<ItemStack> list) {
        return recipe.matches(list);
    }

    public List<ItemStack> getItemsIn() {
        return this.inventoryContents.subList(0, 3);
    }

    @Override
    public List<BlockPos> getAltarPositions() {
        return potentialAltars;
    }

    @Override
    public void removeAltar(BlockPos altarPos) {
        potentialAltars.remove(altarPos);
        this.setChanged();
    }

    @Override
    public void addAltar(BlockPos altarPos) {
        AltarPowerHelper.addAltarByClosest(potentialAltars, level, worldPosition, altarPos);
        this.setChanged();
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public IIntArray getData() {
        return data;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.enchanted.distillery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DistilleryContainer(id, player, this, this.data);
    }

}
