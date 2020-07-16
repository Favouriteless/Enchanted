package com.favouriteless.magicraft.util;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class StaticJSONHelper {

    public static NonNullList<ItemStack> readItemList(JsonArray array) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
            ItemStack stack = stackElementDeserialize(array.get(i));
            nonnulllist.add(stack);
        }

        return nonnulllist;
    }

    private static ItemStack stackElementDeserialize(@Nullable JsonElement json) {
        if (json != null && !json.isJsonNull()) {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                return deserializeItem(jsonObject);
            } else {
                throw new JsonSyntaxException("Expected 1 item");
            }
        } else {
            throw new JsonSyntaxException("input cannot be null");
        }
    }

    private static ItemStack deserializeItem(JsonObject json) {
        if (json.has("item")) {
            ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(json, "item"));
            ItemStack stack = Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + resourcelocation + "'")).getDefaultInstance();
            stack.setCount(Math.max(1, JSONUtils.getInt(json, "count")));
            return stack;
        } else {
            throw new JsonParseException("A Recipe entry needs an input");
        }
    }

}