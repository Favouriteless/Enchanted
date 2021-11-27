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

import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class WitchCauldronSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WitchCauldronRecipe> {

    @Override
    public WitchCauldronRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

        NonNullList<Item> itemsIn = StaticJSONHelper.readItemList(JSONUtils.getAsJsonArray(json, "inputs"));
        ItemStack itemOut = StaticJSONHelper.stackElementDeserialize(JSONUtils.getAsJsonObject(json, "output"));
        int power = JSONUtils.getAsInt(json, "power");

        return new WitchCauldronRecipe(recipeId, itemsIn, itemOut, power);
    }

    @Nullable
    @Override
    public WitchCauldronRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {

        int inSize = buffer.readInt();
        NonNullList<Item> itemsIn = NonNullList.create();
        for (int x = 0; x < inSize; ++x) {
            itemsIn.add(ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation()));
        }
        ItemStack itemOut = buffer.readItem();
        int power = buffer.readInt();

        return new WitchCauldronRecipe(recipeId, itemsIn, itemOut, power);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, WitchCauldronRecipe recipe) {

        buffer.writeInt(recipe.getItemsIn().size());
        for (Item item : recipe.getItemsIn()) {
            buffer.writeResourceLocation(item.getRegistryName());
        }
        buffer.writeItem(recipe.getResultItem());
        buffer.writeInt(recipe.getPower());

    }

}
