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

package com.favouriteless.enchanted.core.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;

public class StaticJSONHelper {

    public static NonNullList<ItemStack> readItemStackList(JsonArray array) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
            ItemStack stack = CraftingHelper.getItemStack(array.get(i).getAsJsonObject(), true);
            nonnulllist.add(stack);
        }

        return nonnulllist;
    }

    public static NonNullList<Ingredient> readIngredientList(JsonArray array) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
            Ingredient stack = Ingredient.fromJson(array.get(i));
            nonnulllist.add(stack);
        }

        return nonnulllist;
    }

    public static NonNullList<Item> readItemList(JsonArray array) {
        NonNullList<Item> nonnulllist = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
            Item item = CraftingHelper.getItemStack(array.get(i).getAsJsonObject(), true).getItem();
            nonnulllist.add(item);
        }

        return nonnulllist;
    }

    public static int[] deserializeColour(JsonObject json) {
        if(json.has("red") && json.has("green") && json.has("blue")) {
            int red = GsonHelper.getAsInt(json, "red");
            int green = GsonHelper.getAsInt(json, "green");
            int blue = GsonHelper.getAsInt(json, "blue");

            return new int[] { red, green, blue };
        } else {
            throw new JsonParseException("Invalid colour in json");
        }
    }

}