package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.datagen.builders.SingleRecipeTemplateBuilder;
import net.minecraft.resources.ResourceLocation;

public class DistilleryPageBuilder {

    public static final ResourceLocation ID = Enchanted.id("page/distillery");

    public static SingleRecipeTemplateBuilder of(ResourceLocation recipe) {
        return new SingleRecipeTemplateBuilder(ID, recipe);
    }

    public static SingleRecipeTemplateBuilder of(String recipe) {
        return new SingleRecipeTemplateBuilder(ID, recipe);
    }

}
