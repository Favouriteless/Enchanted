/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.api.patchouli.processors;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class WitchOvenRecipeProcessor implements IComponentProcessor {

	private WitchOvenRecipe byproductRecipe;
	private ItemStack itemIn;
	private ItemStack resultItem;

	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();

		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		byproductRecipe = (WitchOvenRecipe)recipeManager.byKey(new ResourceLocation(recipeId)).orElseThrow(IllegalArgumentException::new);
		itemIn = variables.get("itemIn").as(ItemStack.class);
		resultItem = variables.get("resultItem").as(ItemStack.class);
	}

	@Override
	public IVariable process(String key) {

		if(key.startsWith("input"))
			return IVariable.from(itemIn);
		else if(key.startsWith("jar"))
			return IVariable.from(new ItemStack(EnchantedItems.CLAY_JAR.get(), byproductRecipe.getJarsNeeded()));
		else if(key.startsWith("result"))
			return IVariable.from(resultItem);
		else if(key.startsWith("byproduct"))
			return IVariable.from(byproductRecipe.getResultItem());

		return null;
	}
}
