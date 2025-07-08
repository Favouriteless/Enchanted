package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.recipe;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.common.datagen.builders.SingleRecipeTemplateBuilder;
import net.minecraft.resources.ResourceLocation;

public class ByproductRecipeBuilder {

    public static final ResourceLocation ID = Enchanted.id("recipe/byproduct");

    public static SingleRecipeTemplateBuilder of(ResourceLocation recipe) {
        return new SingleRecipeTemplateBuilder(ID, recipe);
    }

    public static SingleRecipeTemplateBuilder of(String recipe) {
        return new SingleRecipeTemplateBuilder(ID, recipe);
    }

}
