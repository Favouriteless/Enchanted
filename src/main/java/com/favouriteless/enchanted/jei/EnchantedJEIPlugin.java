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


package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.menus.DistilleryMenu;
import com.favouriteless.enchanted.common.menus.SpinningWheelMenu;
import com.favouriteless.enchanted.common.menus.WitchOvenMenu;
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import com.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class EnchantedJEIPlugin implements IModPlugin {
    private static final ResourceLocation locationOven = new ResourceLocation("enchanted", "witch_oven_category");
    private static final RecipeType<WitchOvenRecipe> recipeTypeOven = new RecipeType(locationOven, WitchOvenRecipe.class);

    private static final ResourceLocation locationDistillery = new ResourceLocation("enchanted", "distillery_category");
    private static final RecipeType<DistilleryRecipe> recipeTypeDistillery = new RecipeType(locationDistillery, DistilleryRecipe.class);

    private static final ResourceLocation locationSpinningWheel = new ResourceLocation("enchanted", "spinning_wheel_category");
    private static final RecipeType<SpinningWheelRecipe> recipeTypeSpinningWheel = new RecipeType(locationSpinningWheel, SpinningWheelRecipe.class);

    private static final ResourceLocation locationWitchCauldron= new ResourceLocation("enchanted", "witch_cauldron_category");
    private static final RecipeType<WitchCauldronRecipe> recipeTypeWitchCauldron = new RecipeType(locationWitchCauldron, WitchCauldronRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return null;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
    }

    @Override
    public void registerFluidSubtypes(ISubtypeRegistration registration) {
        IModPlugin.super.registerFluidSubtypes(registration);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        IModPlugin.super.registerIngredients(registration);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new WitchOvenCategory(registration.getJeiHelpers(), recipeTypeOven));
        registration.addRecipeCategories(new DistilleryCategory(registration.getJeiHelpers(),recipeTypeDistillery));
        registration.addRecipeCategories(new SpinningWheelCategory(registration.getJeiHelpers(),recipeTypeSpinningWheel));
        registration.addRecipeCategories(new WitchCauldronCategory(registration.getJeiHelpers(),recipeTypeWitchCauldron));

    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IModPlugin.super.registerVanillaCategoryExtensions(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager m = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(recipeTypeOven, m.getAllRecipesFor(EnchantedRecipeTypes.WITCH_OVEN));
        registration.addRecipes(recipeTypeDistillery, m.getAllRecipesFor(EnchantedRecipeTypes.DISTILLERY));
        registration.addRecipes(recipeTypeSpinningWheel, m.getAllRecipesFor(EnchantedRecipeTypes.SPINNING_WHEEL));
        registration.addRecipes(recipeTypeWitchCauldron, m.getAllRecipesFor(EnchantedRecipeTypes.WITCH_CAULDRON));

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(WitchOvenMenu.class, recipeTypeOven, 0, 1, 5, 36);
        registration.addRecipeTransferHandler(DistilleryMenu.class, recipeTypeDistillery, 1, 2, 7, 36);
        registration.addRecipeTransferHandler(SpinningWheelMenu.class, recipeTypeSpinningWheel, 0, 3, 4, 36);
        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.WITCH_OVEN.get()), recipeTypeOven);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.DISTILLERY.get()), recipeTypeDistillery);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.SPINNING_WHEEL.get()), recipeTypeSpinningWheel);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.WITCH_CAULDRON.get()), recipeTypeWitchCauldron);

        IModPlugin.super.registerRecipeCatalysts(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        IModPlugin.super.registerGuiHandlers(registration);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
        IModPlugin.super.registerAdvanced(registration);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IModPlugin.super.onRuntimeAvailable(jeiRuntime);
    }
}
