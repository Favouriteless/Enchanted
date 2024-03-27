package com.favouriteless.enchanted.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;

public class JSONHelper {

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