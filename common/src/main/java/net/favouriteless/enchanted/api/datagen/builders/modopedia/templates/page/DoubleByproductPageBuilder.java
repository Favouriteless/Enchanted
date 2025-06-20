package net.favouriteless.enchanted.api.datagen.builders.modopedia.templates.page;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.modopedia.datagen.builders.DoubleRecipeTemplateBuilder;
import net.minecraft.resources.ResourceLocation;

public class DoubleByproductPageBuilder {

    public static final ResourceLocation ID = Enchanted.id("page/double_byproduct");

    public static DoubleRecipeTemplateBuilder of(ResourceLocation recipe, ResourceLocation recipe2) {
        return new DoubleRecipeTemplateBuilder(ID, recipe, recipe2);
    }

    public static DoubleRecipeTemplateBuilder of(ResourceLocation recipe, String recipe2) {
        return new DoubleRecipeTemplateBuilder(ID, recipe, recipe2);
    }

    public static DoubleRecipeTemplateBuilder of(String recipe, ResourceLocation recipe2) {
        return new DoubleRecipeTemplateBuilder(ID, recipe, recipe2);
    }

    public static DoubleRecipeTemplateBuilder of(String recipe, String recipe2) {
        return new DoubleRecipeTemplateBuilder(ID, recipe, recipe2);
    }

}
