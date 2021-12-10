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

import com.favouriteless.enchanted.api.altar.AltarPowerHelper;
import com.favouriteless.enchanted.common.containers.DistilleryContainer;
import com.favouriteless.enchanted.common.recipes.distillery.DistilleryRecipe;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DistilleryTileEntity extends FurnaceTileEntityBase implements IAltarPowerConsumer {

    private DistilleryRecipe currentRecipe;
    private final List<BlockPos> potentialAltars = new ArrayList<>();

    public DistilleryTileEntity(TileEntityType<?> typeIn) {
        super(typeIn, NonNullList.withSize(7, ItemStack.EMPTY));
    }

    public DistilleryTileEntity() {
        this(EnchantedTileEntities.DISTILLERY.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.distillery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DistilleryContainer(id, player, this, this.furnaceData);
    }

    @Override
    public void tick() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.level != null && !this.level.isClientSide) {
            this.matchRecipe();
            AltarTileEntity altar = AltarPowerHelper.tryGetAltar(level, potentialAltars);

            if(this.canDistill(this.currentRecipe) && altar != null) {
                if(altar.currentPower > 10.0D) {
                    altar.currentPower -= 10.0D;
                    this.burnTime = 1;
                    this.cookTime++;


                    if(this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.distill(this.currentRecipe);
                    }
                }
            }
            else {
                this.burnTime = 0;
                this.cookTime = 0;
            }


            if (flag != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if (flag1) {
            this.setChanged();
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

    public DistilleryRecipe getCurrentRecipe() { return this.currentRecipe; }

    public List<ItemStack> getItemsIn() {
        return this.inventoryContents.subList(0, 3);
    }

    @Override
    protected CompoundNBT saveAdditional(CompoundNBT nbt) {
        return AltarPowerHelper.savePosTag(potentialAltars, nbt);
    }

    @Override
    protected void loadAdditional(CompoundNBT nbt) {
        AltarPowerHelper.loadPosTag(potentialAltars, nbt);
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
}
