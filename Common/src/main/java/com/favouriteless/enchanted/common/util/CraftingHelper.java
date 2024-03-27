package com.favouriteless.enchanted.common.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

/**
 * Alternative to Forge's CraftingHelper, for use in common code. Most of this is modified Forge code.
 */
public class CraftingHelper {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public static ItemStack getItemStack(JsonObject json, boolean readNbt) {
		return getItemStack(json, readNbt, false);
	}

	public static ItemStack getItemStack(JsonObject json, boolean readNbt, boolean disallowAir) {
		String name = GsonHelper.getAsString(json, "item");
		Item item = getItem(name, disallowAir);

		ItemStack out = new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));

		if (readNbt && json.has("nbt"))
			out.setTag(getNBT(json.get("nbt")));

		return out;
	}

	public static Item getItem(String id, boolean disallowAir) {
		ResourceLocation key = new ResourceLocation(id);
		if(!Registry.ITEM.containsKey(key))
			throw new JsonSyntaxException("Unknown item '" + id + "'");

		Item item = Registry.ITEM.get(key);

		if (disallowAir && item == Items.AIR)
			throw new JsonSyntaxException("Invalid item: " + id);

		return Objects.requireNonNull(item);
	}

	public static CompoundTag getNBT(JsonElement element) {
		try {
			if (element.isJsonObject())
				return TagParser.parseTag(GSON.toJson(element));
			else
				return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
		}
		catch (CommandSyntaxException exception) {
			throw new JsonSyntaxException("Invalid NBT Entry: " + exception);
		}
	}

	public static boolean ingredientsMatch(Ingredient ingredient, Ingredient other) {
		ItemStack[] items = ingredient.getItems();
		for(ItemStack stack : items) {
			if(other.test(stack))
				return true;
		}
		return false;
	}

}
