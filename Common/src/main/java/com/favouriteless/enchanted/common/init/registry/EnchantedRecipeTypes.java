package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.recipes.*;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class EnchantedRecipeTypes {

    public static Supplier<RecipeType<WitchOvenRecipe>> WITCH_OVEN = registerType("witch_oven");
    public static Supplier<WitchOvenRecipe.Serializer> WITCH_OVEN_SERIALIZER = EnchantedRecipeTypes.registerSerializer("witch_oven", WitchOvenRecipe.Serializer::new);

    public static Supplier<RecipeType<DistilleryRecipe>> DISTILLERY = registerType("distillery");
    public static Supplier<DistilleryRecipe.Serializer> DISTILLERY_SERIALIZER = EnchantedRecipeTypes.registerSerializer("distillery", DistilleryRecipe.Serializer::new);

    public static Supplier<RecipeType<WitchCauldronRecipe>> WITCH_CAULDRON = registerType("witch_cauldron");
    public static Supplier<WitchCauldronRecipe.Serializer> WITCH_CAULDRON_SERIALIZER = EnchantedRecipeTypes.registerSerializer("witch_cauldron", WitchCauldronRecipe.Serializer::new);

    public static Supplier<RecipeType<KettleRecipe>> KETTLE = registerType("kettle");
    public static Supplier<KettleRecipe.Serializer> KETTLE_SERIALIZER = EnchantedRecipeTypes.registerSerializer("kettle", KettleRecipe.Serializer::new);

    public static Supplier<RecipeType<SpinningWheelRecipe>> SPINNING_WHEEL = registerType("spinning_wheel");
    public static Supplier<SpinningWheelRecipe.Serializer> SPINNING_WHEEL_SERIALIZER = EnchantedRecipeTypes.registerSerializer("spinning_wheel", SpinningWheelRecipe.Serializer::new);


    
    private static <T extends RecipeSerializer<?>> Supplier<T> registerSerializer(String name, Supplier<T> serializerSupplier) {
        return Services.COMMON_REGISTRY.register(Registry.RECIPE_SERIALIZER, name, serializerSupplier);
    }

    private static <T extends Recipe<?>> Supplier<RecipeType<T>> registerType(String name) {
        return Services.COMMON_REGISTRY.register(Registry.RECIPE_TYPE, name, () -> new RecipeType<T>() {
            @Override
            public String toString() {
                return name;
            }
        });
    }

    public static void load() {} // Method which exists purely to load the class.

}