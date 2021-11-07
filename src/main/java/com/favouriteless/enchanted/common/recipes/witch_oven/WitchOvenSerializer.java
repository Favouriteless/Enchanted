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

package com.favouriteless.enchanted.common.recipes.witch_oven;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class WitchOvenSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WitchOvenRecipe> {

    @Override
    public WitchOvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

        ItemStack itemIn = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "ingredient"))));
        ItemStack itemOut = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "result"))));

        int jarsNeeded = JSONUtils.getAsInt(json, "jarsneeded", 1);

        return new WitchOvenRecipe(recipeId, itemIn, itemOut, jarsNeeded);
    }

    @Nullable
    @Override
    public WitchOvenRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        ItemStack itemIn = buffer.readItem();
        ItemStack itemOut = buffer.readItem();
        int jarsNeeded = buffer.readInt();

        return new WitchOvenRecipe(recipeId, itemIn, itemOut, jarsNeeded);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, WitchOvenRecipe recipe) {
        buffer.writeItem(recipe.getInput());
        buffer.writeItem(recipe.getOutput());
        buffer.writeInt(recipe.getJarsNeeded());
    }

}