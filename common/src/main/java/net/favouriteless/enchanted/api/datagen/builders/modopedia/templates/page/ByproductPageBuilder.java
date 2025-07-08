package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.common.datagen.builders.SingleRecipeTemplateBuilder;
import net.minecraft.resources.ResourceLocation;

public class ByproductPageBuilder {

    public static final ResourceLocation ID = Enchanted.id("page/byproduct");

    public static SingleRecipeTemplateBuilder of(ResourceLocation recipe) {
        return new SingleRecipeTemplateBuilder(ID, recipe);
    }

    public static SingleRecipeTemplateBuilder of(String recipe) {
        return new SingleRecipeTemplateBuilder(ID, recipe);
    }

}
