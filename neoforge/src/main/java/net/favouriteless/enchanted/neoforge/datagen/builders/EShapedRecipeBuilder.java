package net.favouriteless.enchanted.neoforge.datagen.builders;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

public class EShapedRecipeBuilder extends ShapedRecipeBuilder {

    public EShapedRecipeBuilder(RecipeCategory category, ItemStack result) {
        super(category, result);
    }

    public EShapedRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        super(category, result, count);
    }

    public static EShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result) {
        return shaped(category, result, 1);
    }

    public static EShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int count) {
        return new EShapedRecipeBuilder(category, result, count);
    }

    public static EShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
        return new EShapedRecipeBuilder(category, result);
    }

    @Override
    public ShapedRecipePattern ensureValid(ResourceLocation location) {
        return ShapedRecipePattern.of(this.key, this.rows);
    }

}
