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

package com.favouriteless.enchanted.common.recipes.witch_cauldron;

import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WitchCauldronRecipe implements IRecipe<IInventory> {

    protected final IRecipeType<?> type;
    protected final ResourceLocation id;

    private final NonNullList<ItemStack> itemsIn;
    private final ItemStack itemOut;
    private final int power;

    public WitchCauldronRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, ItemStack itemOut, int power) {
        this.type = EnchantedRecipeTypes.WITCH_CAULDRON;
        this.id = id;

        this.itemsIn = itemsIn;
        this.itemOut = itemOut;
        this.power = power;
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        for(ItemStack stack : itemsIn) {
            if(stack != ItemStack.EMPTY) {
                boolean flag = false;

                for(int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stackIn = inventory.getItem(i);
                    if(stack.sameItem(stackIn) && stack.getCount() == stackIn.getCount()) {
                        if(!flag) {
                            flag = true;
                        }
                        else {
                            return false; // Can't have too many of an item
                        }
                    }
                }
                if(!flag) return false; // Item was missing
            }
        }
        return true;
    }

    public NonNullList<ItemStack> getItemsIn() {
        return itemsIn;
    }

    public int getPower() {
        return power;
    }

    @Override
    public ItemStack getResultItem() {
        return itemOut;
    }



    @Override
    public ItemStack assemble(IInventory pInv) {
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
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
