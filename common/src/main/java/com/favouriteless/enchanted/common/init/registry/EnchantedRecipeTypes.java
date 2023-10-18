package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.recipes.*;
import com.favouriteless.enchanted.platform.RegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class EnchantedRecipeTypes {

    public static RecipeType<WitchOvenRecipe> WITCH_OVEN = RecipeType.register("enchanted:witch_oven");
    public static Supplier<WitchOvenRecipe.Serializer> WITCH_OVEN_SERIALIZER = register("witch_oven", WitchOvenRecipe.Serializer::new);

    public static RecipeType<DistilleryRecipe> DISTILLERY = RecipeType.register("enchanted:distillery");
    public static Supplier<DistilleryRecipe.Serializer> DISTILLERY_SERIALIZER = register("distillery", DistilleryRecipe.Serializer::new);

    public static RecipeType<WitchCauldronRecipe> WITCH_CAULDRON = RecipeType.register("enchanted:witch_cauldron");
    public static Supplier<WitchCauldronRecipe.Serializer> WITCH_CAULDRON_SERIALIZER = register("witch_cauldron", WitchCauldronRecipe.Serializer::new);

    public static RecipeType<KettleRecipe> KETTLE = RecipeType.register("enchanted:kettle");
    public static Supplier<KettleRecipe.Serializer> KETTLE_SERIALIZER = register("kettle", KettleRecipe.Serializer::new);

    public static RecipeType<SpinningWheelRecipe> SPINNING_WHEEL = RecipeType.register("enchanted:spinning_wheel");
    public static Supplier<SpinningWheelRecipe.Serializer> SPINNING_WHEEL_SERIALIZER = register("spinning_wheel", SpinningWheelRecipe.Serializer::new);


    
    private static <T extends RecipeSerializer<?>> Supplier<T> register(String name, Supplier<T> serializerSupplier) {
        return RegistryHandler.register(Registry.RECIPE_SERIALIZER, name, serializerSupplier);
    }

    public static void load() {} // Method which exists purely to load the class.

}