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

package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.client.screens.DistilleryScreen;
import com.favouriteless.enchanted.client.screens.SpinningWheelScreen;
import com.favouriteless.enchanted.client.screens.WitchOvenScreen;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.menus.SpinningWheelMenu;
import com.favouriteless.enchanted.common.menus.WitchOvenMenu;
import com.favouriteless.enchanted.jei.categories.*;
import com.favouriteless.enchanted.jei.container_handlers.DistilleryContainerHandler;
import com.favouriteless.enchanted.jei.container_handlers.SpinningWheelContainerHandler;
import com.favouriteless.enchanted.jei.container_handlers.WitchOvenContainerHandler;
import com.favouriteless.enchanted.jei.recipes.JEIMutandisRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

import static com.favouriteless.enchanted.jei.JEIRecipeTypes.*;

@JeiPlugin
public class EnchantedJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("enchanted","jei_plugin");
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
        registration.addRecipeCategories(new WitchOvenCategory(registration.getJeiHelpers(), RECIPE_TYPE_WITCH_OVEN));
        registration.addRecipeCategories(new DistilleryCategory(registration.getJeiHelpers(), RECIPE_TYPE_DISTILLERY));
        registration.addRecipeCategories(new SpinningWheelCategory(registration.getJeiHelpers(), RECIPE_TYPE_SPINNING_WHEEL));
        registration.addRecipeCategories(new WitchCauldronCategory(registration.getJeiHelpers(), RECIPE_TYPE_WITCH_CAULDRON));
        registration.addRecipeCategories(new KettleCategory(registration.getJeiHelpers(), RECIPE_TYPE_KETTLE));
        registration.addRecipeCategories(new RiteCategory(registration.getJeiHelpers(), RECIPE_TYPE_RITE));
        registration.addRecipeCategories(new MutandisCategory(registration.getJeiHelpers(), RECIPE_TYPE_MUTANDIS,new ItemStack(EnchantedItems.MUTANDIS.get()),new TranslatableComponent("jei.enchanted.mutandis")));
        registration.addRecipeCategories(new MutandisCategory(registration.getJeiHelpers(), RECIPE_TYPE_MUTANDIS_EXTREMIS,new ItemStack(EnchantedItems.MUTANDIS_EXTREMIS.get()),new TranslatableComponent("jei.enchanted.mutandis_extremis")));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        IModPlugin.super.registerVanillaCategoryExtensions(registration);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(RECIPE_TYPE_WITCH_OVEN, recipeManager.getAllRecipesFor(EnchantedRecipeTypes.WITCH_OVEN));
        registration.addRecipes(RECIPE_TYPE_DISTILLERY, recipeManager.getAllRecipesFor(EnchantedRecipeTypes.DISTILLERY));
        registration.addRecipes(RECIPE_TYPE_SPINNING_WHEEL, recipeManager.getAllRecipesFor(EnchantedRecipeTypes.SPINNING_WHEEL));
        registration.addRecipes(RECIPE_TYPE_WITCH_CAULDRON, recipeManager.getAllRecipesFor(EnchantedRecipeTypes.WITCH_CAULDRON));
        registration.addRecipes(RECIPE_TYPE_KETTLE, recipeManager.getAllRecipesFor(EnchantedRecipeTypes.KETTLE));
        List<AbstractCreateItemRite> ritesCreateItemCrafts = RiteTypes.RITE_TYPES.getEntries()
                .stream()
                .map(rite -> rite.get().create())
                .filter(rite -> rite instanceof AbstractCreateItemRite)
                .map(rite -> (AbstractCreateItemRite)rite)
                .collect(Collectors.toList());
        registration.addRecipes(RECIPE_TYPE_RITE, ritesCreateItemCrafts);

        registration.addRecipes(RECIPE_TYPE_MUTANDIS,
                ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_PLANTS)
                        .stream()
                        .filter((b)->!ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST).contains(b))
                        .map(b->new JEIMutandisRecipe(EnchantedTags.Blocks.MUTANDIS_PLANTS,new ItemStack(b),new TranslatableComponent("jei.enchanted.mutandis.description")))
                        .toList());

        registration.addRecipes(RECIPE_TYPE_MUTANDIS_EXTREMIS,
                ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_PLANTS)
                        .stream()
                        .filter((b)->!ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.MUTANDIS_BLACKLIST).contains(b))
                        .map(b->new JEIMutandisRecipe(EnchantedTags.Blocks.MUTANDIS_EXTREMIS_PLANTS,new ItemStack(b),new TranslatableComponent("jei.enchanted.mutandis.description")))
                        .toList());


        registration.addIngredientInfo(new ItemStack(EnchantedItems.CHALICE_FILLED.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("jei.enchanted.chalice_filled"));
        registration.addIngredientInfo(new ItemStack(EnchantedItems.CHALICE_FILLED_MILK.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("jei.enchanted.chalice_filled_milk"));
        IModPlugin.super.registerRecipes(registration);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(WitchOvenMenu.class, RECIPE_TYPE_WITCH_OVEN, 0, 1, 5, 36);
        registration.addRecipeTransferHandler(new DistilleryTransferInfo());
        registration.addRecipeTransferHandler(SpinningWheelMenu.class, RECIPE_TYPE_SPINNING_WHEEL, 0, 3, 4, 36);
        IModPlugin.super.registerRecipeTransferHandlers(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.WITCH_OVEN.get()), RECIPE_TYPE_WITCH_OVEN);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.DISTILLERY.get()), RECIPE_TYPE_DISTILLERY);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.SPINNING_WHEEL.get()), RECIPE_TYPE_SPINNING_WHEEL);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.WITCH_CAULDRON.get()), RECIPE_TYPE_WITCH_CAULDRON);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.KETTLE.get()), RECIPE_TYPE_KETTLE);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.CHALK_WHITE.get()), RECIPE_TYPE_RITE);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.CHALK_GOLD.get()), RECIPE_TYPE_RITE);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.CHALK_PURPLE.get()), RECIPE_TYPE_RITE);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.CHALK_RED.get()), RECIPE_TYPE_RITE);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.MUTANDIS.get()), RECIPE_TYPE_MUTANDIS);
        registration.addRecipeCatalyst(new ItemStack(EnchantedItems.MUTANDIS_EXTREMIS.get()), RECIPE_TYPE_MUTANDIS_EXTREMIS);
        IModPlugin.super.registerRecipeCatalysts(registration);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(DistilleryScreen.class, new DistilleryContainerHandler());
        registration.addGenericGuiContainerHandler(SpinningWheelScreen.class, new SpinningWheelContainerHandler());
        registration.addGenericGuiContainerHandler(WitchOvenScreen.class, new WitchOvenContainerHandler());
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
