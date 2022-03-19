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

package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class WitchCauldronRecipe implements IRecipe<IInventory> {

    protected final IRecipeType<?> type;
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

    public WitchCauldronRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, ItemStack itemOut, int power, int[] cookingColour, int[] finalColour) {
        this.type = EnchantedRecipeTypes.WITCH_CAULDRON;
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
    public boolean matches(IInventory inventory, World world) {
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
    public boolean fullMatch(IInventory inventory, World world) {
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
        return EnchantedRecipeTypes.WITCH_CAULDRON_SERIALIZER.get();
    }


    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }



    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WitchCauldronRecipe> {

        @Override
        public WitchCauldronRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(JSONUtils.getAsJsonArray(json, "inputs"));
            ItemStack itemOut = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);
            int power = JSONUtils.getAsInt(json, "power");
            int[] cookingColour = StaticJSONHelper.deserializeColour(JSONUtils.getAsJsonObject(json, "cookingColour"));
            int[] finalColour = StaticJSONHelper.deserializeColour(JSONUtils.getAsJsonObject(json, "finalColour"));

            return new WitchCauldronRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
        }

        @Nullable
        @Override
        public WitchCauldronRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {

            int inSize = buffer.readInt();
            NonNullList<ItemStack> itemsIn = NonNullList.create();
            for (int x = 0; x < inSize; ++x) {
                itemsIn.add(buffer.readItem());
            }
            ItemStack itemOut = buffer.readItem();
            int power = buffer.readInt();
            int[] cookingColour = new int[] {(int)buffer.readShort(), (int)buffer.readShort(), (int)buffer.readShort() };
            int[] finalColour = new int[] {(int)buffer.readShort(), (int)buffer.readShort(), (int)buffer.readShort() };

            return new WitchCauldronRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, WitchCauldronRecipe recipe) {

            buffer.writeInt(recipe.getItemsIn().size());
            for (ItemStack item : recipe.getItemsIn()) {
                buffer.writeItem(item);
            }
            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.getPower());
            buffer.writeShort(recipe.getCookingRed());
            buffer.writeShort(recipe.getCookingGreen());
            buffer.writeShort(recipe.getCookingBlue());
            buffer.writeShort(recipe.getFinalRed());
            buffer.writeShort(recipe.getFinalGreen());
            buffer.writeShort(recipe.getFinalBlue());

        }

    }
}
