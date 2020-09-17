package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.recipe.distillery.*;
import com.favouriteless.magicraft.recipe.witch_oven.WitchOvenRecipe;
import com.favouriteless.magicraft.recipe.witch_oven.WitchOvenSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicraftRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Magicraft.MOD_ID);

    public static IRecipeType<DistilleryRecipe> WITCH_OVEN;
    public static RegistryObject<IRecipeSerializer<WitchOvenRecipe>> WITCH_OVEN_SERIALIZER = RECIPE_SERIALIZERS.register("witch_oven", WitchOvenSerializer::new);

    public static IRecipeType<DistilleryRecipe> DISTILLERY;
    public static RegistryObject<IRecipeSerializer<DistilleryRecipe>> DISTILLERY_SERIALIZER = RECIPE_SERIALIZERS.register("distillery", DistillerySerializer::new);

    public static void init() {
        WITCH_OVEN = IRecipeType.register("witch_oven");
        DISTILLERY = IRecipeType.register("distillery");
    }

}
