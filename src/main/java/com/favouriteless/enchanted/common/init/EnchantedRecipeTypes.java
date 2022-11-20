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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.recipes.*;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe.Serializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantedRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Enchanted.MOD_ID);

    public static RecipeType<WitchOvenRecipe> WITCH_OVEN;
    public static RegistryObject<Serializer> WITCH_OVEN_SERIALIZER = RECIPE_SERIALIZERS.register("witch_oven", WitchOvenRecipe.Serializer::new);

    public static RecipeType<DistilleryRecipe> DISTILLERY;
    public static RegistryObject<DistilleryRecipe.Serializer> DISTILLERY_SERIALIZER = RECIPE_SERIALIZERS.register("distillery", DistilleryRecipe.Serializer::new);

    public static RecipeType<WitchCauldronRecipe> WITCH_CAULDRON;
    public static RegistryObject<WitchCauldronRecipe.Serializer> WITCH_CAULDRON_SERIALIZER = RECIPE_SERIALIZERS.register("witch_cauldron", WitchCauldronRecipe.Serializer::new);

    public static RecipeType<KettleRecipe> KETTLE;
    public static RegistryObject<KettleRecipe.Serializer> KETTLE_SERIALIZER = RECIPE_SERIALIZERS.register("kettle", KettleRecipe.Serializer::new);

    public static RecipeType<SpinningWheelRecipe> SPINNING_WHEEL;
    public static RegistryObject<SpinningWheelRecipe.Serializer> SPINNING_WHEEL_SERIALIZER = RECIPE_SERIALIZERS.register("spinning_wheel", SpinningWheelRecipe.Serializer::new);


    public static void init() {
        WITCH_OVEN = RecipeType.register("enchanted:witch_oven");
        DISTILLERY = RecipeType.register("enchanted:distillery");
        WITCH_CAULDRON = RecipeType.register("enchanted:witch_cauldron");
        KETTLE = RecipeType.register("enchanted:kettle");
        SPINNING_WHEEL = RecipeType.register("enchanted:spinning_wheel");
    }

}