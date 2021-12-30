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

package com.favouriteless.enchanted.api.patchouli.processors;

import com.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class CauldronRecipeProcessor implements IComponentProcessor {

	private WitchCauldronRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();

		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		IRecipe<?> recipeIn = recipeManager.byKey(new ResourceLocation(recipeId)).orElseThrow(IllegalArgumentException::new);
		if(!(recipeIn instanceof WitchCauldronRecipe))
			throw new IllegalStateException();
		recipe = (WitchCauldronRecipe)recipeIn;
	}

	@Override
	public IVariable process(String key) {

		if(key.startsWith("result"))
			return IVariable.from(recipe.getResultItem());
		else if(key.startsWith("itemList"))
			return IVariable.from(getItemArray(recipe));
		else if(key.startsWith("power"))
			return IVariable.wrap(recipe.getPower() + " Altar Power");

		return null;
	}

	private static ItemStack[] getItemArray(WitchCauldronRecipe recipe) {
		NonNullList<ItemStack> itemStacks = recipe.getItemsIn();
		ItemStack[] itemArray = new ItemStack[itemStacks.size()];
		for(int i = 0; i < itemArray.length; i++) {
			itemArray[i] = itemStacks.get(i);
		}
		return itemArray;
	}
}
