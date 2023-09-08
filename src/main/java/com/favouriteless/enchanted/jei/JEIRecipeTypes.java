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
import com.favouriteless.enchanted.common.recipes.*;
import com.favouriteless.enchanted.jei.recipes.JEIMutandisRecipe;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class JEIRecipeTypes {

    private static final ResourceLocation locationOven = new ResourceLocation("enchanted", "witch_oven_category");
    public static final RecipeType<WitchOvenRecipe> RECIPE_TYPE_WITCH_OVEN = new RecipeType<>(locationOven, WitchOvenRecipe.class);

    private static final ResourceLocation locationDistillery = new ResourceLocation("enchanted", "distillery_category");
    public static final RecipeType<DistilleryRecipe> RECIPE_TYPE_DISTILLERY = new RecipeType<>(locationDistillery, DistilleryRecipe.class);

    private static final ResourceLocation locationSpinningWheel = new ResourceLocation("enchanted", "spinning_wheel_category");
    public static final RecipeType<SpinningWheelRecipe> RECIPE_TYPE_SPINNING_WHEEL = new RecipeType<>(locationSpinningWheel, SpinningWheelRecipe.class);

    private static final ResourceLocation locationWitchCauldron= new ResourceLocation("enchanted", "witch_cauldron_category");
    public static final RecipeType<WitchCauldronRecipe> RECIPE_TYPE_WITCH_CAULDRON = new RecipeType<>(locationWitchCauldron, WitchCauldronRecipe.class);

    private static final ResourceLocation locationKettle= new ResourceLocation("enchanted", "kettle_category");
    public static final RecipeType<KettleRecipe> RECIPE_TYPE_KETTLE = new RecipeType<>(locationKettle, KettleRecipe.class);

    private static final ResourceLocation locationRite= new ResourceLocation("enchanted", "rite_category");
    public static final RecipeType<AbstractCreateItemRite> RECIPE_TYPE_RITE = new RecipeType<>(locationRite, AbstractCreateItemRite.class);

    private static final ResourceLocation locationMutandis= new ResourceLocation("enchanted", "mutandis_category");
    public static final RecipeType<JEIMutandisRecipe> RECIPE_TYPE_MUTANDIS = new RecipeType<>(locationMutandis, JEIMutandisRecipe.class);

    private static final ResourceLocation locationMutandisExtremis = new ResourceLocation("enchanted", "mutandis_extremis_category");
    public static final RecipeType<JEIMutandisRecipe> RECIPE_TYPE_MUTANDIS_EXTREMIS = new RecipeType<>(locationMutandisExtremis, JEIMutandisRecipe.class);

}
