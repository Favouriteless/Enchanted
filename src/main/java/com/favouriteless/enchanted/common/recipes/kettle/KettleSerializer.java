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

package com.favouriteless.enchanted.common.recipes.kettle;

import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class KettleSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<KettleRecipe> {

    @Override
    public KettleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

        NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(JSONUtils.getAsJsonArray(json, "inputs"));
        ItemStack itemOut = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "output"), true);
        int power = JSONUtils.getAsInt(json, "power");
        int cookingColour = StaticJSONHelper.deserializeColour(JSONUtils.getAsJsonObject(json, "cookingColour"));
        int finalColour = StaticJSONHelper.deserializeColour(JSONUtils.getAsJsonObject(json, "finalColour"));

        return new KettleRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
    }

    @Nullable
    @Override
    public KettleRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {

        int inSize = buffer.readInt();
        NonNullList<ItemStack> itemsIn = NonNullList.create();
        for (int x = 0; x < inSize; ++x) {
            itemsIn.add(buffer.readItem());
        }
        ItemStack itemOut = buffer.readItem();
        int power = buffer.readInt();
        int cookingColour = buffer.readInt();
        int finalColour = buffer.readInt();

        return new KettleRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, KettleRecipe recipe) {

        buffer.writeInt(recipe.getItemsIn().size());
        for (ItemStack item : recipe.getItemsIn()) {
            buffer.writeItem(item);
        }
        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getPower());
        buffer.writeInt(recipe.getCookingColour());
        buffer.writeInt(recipe.getFinalColour());

    }

}
