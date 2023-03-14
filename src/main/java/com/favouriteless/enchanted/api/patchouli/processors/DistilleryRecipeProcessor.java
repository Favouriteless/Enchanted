/*
 *
 *   Copyright (c) 2023. Favouriteless
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

import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class DistilleryRecipeProcessor implements IComponentProcessor {

	private DistilleryRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();

		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		Recipe<?> recipeIn = recipeManager.byKey(new ResourceLocation(recipeId)).orElseThrow(IllegalArgumentException::new);
		if(!(recipeIn instanceof DistilleryRecipe))
			throw new IllegalStateException();
		recipe = (DistilleryRecipe)recipeIn;
	}

	@Override
	public IVariable process(String key) {

		if(key.startsWith("in")) {
			int index = Integer.parseInt(String.valueOf(key.charAt(key.length()-1)));
			NonNullList<ItemStack> stacks = recipe.getItemsIn();
			if(index >= 0 && index < stacks.size()) {
				return IVariable.from(stacks.get(index));
			}
			return IVariable.from(ItemStack.EMPTY);
		}
		else if(key.startsWith("out")) {
			int index = Integer.parseInt(String.valueOf(key.charAt(key.length()-1)));
			NonNullList<ItemStack> stacks = recipe.getItemsOut();
			if(index >= 0 && index < stacks.size()) {
				return IVariable.from(stacks.get(index));
			}
			return IVariable.from(ItemStack.EMPTY);
		}

		return null;
	}
}
