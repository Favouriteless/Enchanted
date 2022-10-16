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

package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class KettleRecipe extends CauldronTypeRecipe {

    public KettleRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, ItemStack itemOut, int power, int[] cookingColour, int[] finalColour) {
        super(EnchantedRecipeTypes.KETTLE, id, itemsIn, itemOut, power, cookingColour, finalColour);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.KETTLE_SERIALIZER.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<KettleRecipe> {

        @Override
        public KettleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "inputs"));
            ItemStack itemOut = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
            int power = GsonHelper.getAsInt(json, "power");
            int[] cookingColour = StaticJSONHelper.deserializeColour(GsonHelper.getAsJsonObject(json, "cookingColour"));
            int[] finalColour = StaticJSONHelper.deserializeColour(GsonHelper.getAsJsonObject(json, "finalColour"));

            return new KettleRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
        }

        @Nullable
        @Override
        public KettleRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            int inSize = buffer.readInt();
            NonNullList<ItemStack> itemsIn = NonNullList.create();
            for (int x = 0; x < inSize; ++x) {
                itemsIn.add(buffer.readItem());
            }
            ItemStack itemOut = buffer.readItem();
            int power = buffer.readInt();
            int[] cookingColour = new int[] {(int)buffer.readShort(), (int)buffer.readShort(), (int)buffer.readShort() };
            int[] finalColour = new int[] {(int)buffer.readShort(), (int)buffer.readShort(), (int)buffer.readShort() };

            return new KettleRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, KettleRecipe recipe) {

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
