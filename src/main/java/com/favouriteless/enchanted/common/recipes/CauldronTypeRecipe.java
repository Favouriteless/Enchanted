/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.recipes;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class CauldronTypeRecipe implements Recipe<Container> {

    protected final RecipeType<?> type;
    protected final ResourceLocation id;

    private final NonNullList<ItemStack> itemsIn;
    private final ItemStack itemOut;
    private final int power;

    private final int cookingRed;
    private final int cookingGreen;
    private final int cookingBlue;
    private final int finalRed;
    private final int finalGreen;
    private final int finalBlue;

    public CauldronTypeRecipe(RecipeType<?> type, ResourceLocation id, NonNullList<ItemStack> itemsIn, ItemStack itemOut, int power, int[] cookingColour, int[] finalColour) {
        this.type = type;
        this.id = id;

        this.itemsIn = itemsIn;
        this.itemOut = itemOut;
        this.power = power;

        this.cookingRed = cookingColour[0];
        this.cookingGreen = cookingColour[1];
        this.cookingBlue = cookingColour[2];
        this.finalRed = finalColour[0];
        this.finalGreen = finalColour[1];
        this.finalBlue = finalColour[2];
    }

    /**
     * Returns true if inventory is a partial match for this recipe
     * @param inventory
     * @param world
     * @return isMatch
     */
    @Override
    public boolean matches(Container inventory, Level world) {
        if(inventory.isEmpty() || inventory.getContainerSize() > itemsIn.size())
            return false; // Too many items

        for(int i = 0; i < itemsIn.size() && i < inventory.getContainerSize(); i++) {
            ItemStack itemIn = itemsIn.get(i);
            ItemStack inventoryItem = inventory.getItem(i);

            if(!ItemStack.matches(itemIn, inventoryItem))
                return false;
        }
        return true;
    }

    /**
     * Returns true if inventory is a full match to this recipe
     * @param inventory
     * @param world
     * @return isMatch
     */
    public boolean fullMatch(Container inventory, Level world) {
        if(inventory.getContainerSize() != itemsIn.size()) // Same number of items
            return false;

        for(int i = 0; i < itemsIn.size(); i++) {
            ItemStack itemIn = itemsIn.get(i);
            ItemStack inventoryItem = inventory.getItem(i);
            if(!ItemStack.matches(itemIn, inventoryItem))
                return false;
        }

        return true;
    }

    public NonNullList<ItemStack> getItemsIn() {
        return itemsIn;
    }

    public int getPower() {
        return power;
    }

    public int getCookingRed() {
        return cookingRed;
    }

    public int getCookingGreen() {
        return cookingGreen;
    }

    public int getCookingBlue() {
        return cookingBlue;
    }

    public int getFinalRed() {
        return finalRed;
    }

    public int getFinalGreen() {
        return finalGreen;
    }

    public int getFinalBlue() {
        return finalBlue;
    }

    @Override
    public ItemStack getResultItem() {
        return itemOut;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
