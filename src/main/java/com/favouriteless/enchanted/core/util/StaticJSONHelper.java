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

package com.favouriteless.enchanted.core.util;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.awt.*;

public class StaticJSONHelper {

    public static NonNullList<ItemStack> readItemStackList(JsonArray array) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
            ItemStack stack = stackElementDeserialize(array.get(i));
            nonnulllist.add(stack);
        }

        return nonnulllist;
    }

    public static ItemStack stackElementDeserialize(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                return deserializeItemStack(jsonObject);
            } else {
                throw new JsonSyntaxException("Expected 1 item");
            }
        } else {
            throw new JsonSyntaxException("JSON cannot be null");
        }
    }

    private static ItemStack deserializeItemStack(JsonObject json) {
        if (json.has("item")) {
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "item"))));
            if(json.has("count")) {
                stack.setCount(Math.max(1, JSONUtils.getAsInt(json, "count")));
            }
            else {
                stack.setCount(1);
            }
            return stack;
        } else {
            throw new JsonParseException("A Recipe entry needs an input");
        }
    }

    public static NonNullList<Item> readItemList(JsonArray array) {
        NonNullList<Item> nonnulllist = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
            Item item = itemElementDeserialize(array.get(i));
            nonnulllist.add(item);
        }

        return nonnulllist;
    }

    public static Item itemElementDeserialize(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                return deserializeItem(jsonObject);
            } else {
                throw new JsonSyntaxException("Expected 1 item");
            }
        } else {
            throw new JsonSyntaxException("JSON cannot be null");
        }
    }


    private static Item deserializeItem(JsonObject json) {
        if (json.has("item")) {
            return ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getAsString(json, "item")));
        } else {
            throw new JsonParseException("A Recipe entry needs an input");
        }
    }

    public static int deserializeColour(JsonObject json) {
        if(json.has("red") && json.has("green") && json.has("blue")) {
            int red = JSONUtils.getAsInt(json, "red");
            int green = JSONUtils.getAsInt(json, "green");
            int blue = JSONUtils.getAsInt(json, "blue");

            return 0xFF000000 | (red << 16) & 0x00FF0000 | (green << 8) & 0x0000FF00 | blue & 0x000000FF;
        } else {
            throw new JsonParseException("Invalid colour in json");
        }
    }

}