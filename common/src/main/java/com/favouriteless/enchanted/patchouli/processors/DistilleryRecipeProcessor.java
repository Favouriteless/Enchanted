package com.favouriteless.enchanted.patchouli.processors;

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

		int index = Integer.parseInt(String.valueOf(key.charAt(key.length() - 1)));
		if(key.startsWith("in")) {
            NonNullList<ItemStack> stacks = recipe.getItemsIn();
			if(index >= 0 && index < stacks.size()) {
				return IVariable.from(stacks.get(index));
			}
			return IVariable.from(ItemStack.EMPTY);
		}
		else if(key.startsWith("out")) {
            NonNullList<ItemStack> stacks = recipe.getItemsOut();
			if(index >= 0 && index < stacks.size()) {
				return IVariable.from(stacks.get(index));
			}
			return IVariable.from(ItemStack.EMPTY);
		}

		return null;
	}
}
