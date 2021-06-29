package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.recipes.distillery.DistillerySerializer;
import com.favouriteless.enchanted.common.recipes.witch_oven.WitchOvenRecipe;
import com.favouriteless.enchanted.common.recipes.witch_oven.WitchOvenSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedRecipeTypes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Enchanted.MOD_ID);

    public static IRecipeType<WitchOvenRecipe> WITCH_OVEN;
    public static RegistryObject<WitchOvenSerializer> WITCH_OVEN_SERIALIZER = RECIPE_SERIALIZERS.register("witch_oven", WitchOvenSerializer::new);

    public static IRecipeType<WitchOvenRecipe> DISTILLERY;
    public static RegistryObject<DistillerySerializer> DISTILLERY_SERIALIZER = RECIPE_SERIALIZERS.register("distillery", DistillerySerializer::new);


    public static void init() {
        WITCH_OVEN = IRecipeType.register("witch_oven");
        DISTILLERY = IRecipeType.register("distillery");
    }

}