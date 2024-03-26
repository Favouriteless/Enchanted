package com.favouriteless.enchanted.patchouli.processors;

import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
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
			return IVariable.from(new ItemStack(EnchantedItems.CLAY_JAR.get()));
		else if(key.startsWith("result"))
			return IVariable.from(resultItem);
		else if(key.startsWith("byproduct"))
			return IVariable.from(byproductRecipe.getResultItem());

		return null;
	}
}
