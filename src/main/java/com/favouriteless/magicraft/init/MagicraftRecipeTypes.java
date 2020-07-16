package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.recipe.distillery.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicraftRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Magicraft.MOD_ID);

    public static IRecipeType<DistilleryRecipe> DISTILLERY;
    public static RegistryObject<IRecipeSerializer<DistilleryRecipe>> DISTILLERY_SERIALIZER = RECIPE_SERIALIZERS.register("distillery", DistillerySerializer::new);;

    public static void init() {
        DISTILLERY = IRecipeType.register("distillery");
    }

}
