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

    private static final ResourceLocation LOCATION_WITCH_OVEN = new ResourceLocation("enchanted", "witch_oven_category");
    public static final RecipeType<WitchOvenRecipe> RECIPE_TYPE_WITCH_OVEN = new RecipeType<>(LOCATION_WITCH_OVEN, WitchOvenRecipe.class);

    private static final ResourceLocation LOCATION_DISTILLERY = new ResourceLocation("enchanted", "distillery_category");
    public static final RecipeType<DistilleryRecipe> RECIPE_TYPE_DISTILLERY = new RecipeType<>(LOCATION_DISTILLERY, DistilleryRecipe.class);

    private static final ResourceLocation LOCATION_SPINNING_WHEEL = new ResourceLocation("enchanted", "spinning_wheel_category");
    public static final RecipeType<SpinningWheelRecipe> RECIPE_TYPE_SPINNING_WHEEL = new RecipeType<>(LOCATION_SPINNING_WHEEL, SpinningWheelRecipe.class);

    private static final ResourceLocation LOCATION_WITCH_CAULDRON = new ResourceLocation("enchanted", "witch_cauldron_category");
    public static final RecipeType<WitchCauldronRecipe> RECIPE_TYPE_WITCH_CAULDRON = new RecipeType<>(LOCATION_WITCH_CAULDRON, WitchCauldronRecipe.class);

    private static final ResourceLocation LOCATION_KETTLE = new ResourceLocation("enchanted", "kettle_category");
    public static final RecipeType<KettleRecipe> RECIPE_TYPE_KETTLE = new RecipeType<>(LOCATION_KETTLE, KettleRecipe.class);

    private static final ResourceLocation LOCATION_RITE = new ResourceLocation("enchanted", "rite_category");
    public static final RecipeType<AbstractCreateItemRite> RECIPE_TYPE_RITE = new RecipeType<>(LOCATION_RITE, AbstractCreateItemRite.class);

    private static final ResourceLocation LOCATION_MUTANDIS = new ResourceLocation("enchanted", "mutandis_category");
    public static final RecipeType<JEIMutandisRecipe> RECIPE_TYPE_MUTANDIS = new RecipeType<>(LOCATION_MUTANDIS, JEIMutandisRecipe.class);

    private static final ResourceLocation LOCATION_MUTANDIS_EXTREMIS = new ResourceLocation("enchanted", "mutandis_extremis_category");
    public static final RecipeType<JEIMutandisRecipe> RECIPE_TYPE_MUTANDIS_EXTREMIS = new RecipeType<>(LOCATION_MUTANDIS_EXTREMIS, JEIMutandisRecipe.class);

}
