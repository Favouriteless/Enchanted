package com.favouriteless.enchanted.common.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class StaticCraftingHelper {

	public static boolean ingredientsMatch(Ingredient ingredient, Ingredient other) {
		ItemStack[] items = ingredient.getItems();
		for(ItemStack stack : items) {
			if(other.test(stack))
				return true;
		}
		return false;
	}

}
