package net.favouriteless.enchanted.api.datagen.builders.recipe;

import net.favouriteless.enchanted.common.recipes.ByproductRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;

public class ByproductRecipeBuilder extends ERecipeBuilder {

    private final ItemStack result;
    private final ItemLike[] items;
    private final TagKey<Item> tag;

    private ByproductRecipeBuilder(ItemStack result, ItemLike[] items, TagKey<Item> tag) {
        super("byproduct");
        this.result = result;
        this.items = items;
        this.tag = tag;
    }

    public static ByproductRecipeBuilder create(ItemStack result, ItemLike... itemLikes) {
        return new ByproductRecipeBuilder(result, itemLikes, null);
    }

    public static ByproductRecipeBuilder create(ItemStack result, ItemStack... stacks) {
        return new ByproductRecipeBuilder(result, Arrays.stream(stacks).map(ItemStack::getItem).toArray(ItemLike[]::new), null);
    }

    public static ByproductRecipeBuilder create(ItemStack result, TagKey<Item> tag) {
        return new ByproductRecipeBuilder(result, null, tag);
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(id, new ByproductRecipe(tag == null ? Ingredient.of(items) : Ingredient.of(tag), result), null);
    }

    @Override
    protected String getDefaultName() {
        String prefix = BuiltInRegistries.ITEM.getKey(result.getItem()).getPath();
        String suffix = tag != null ? tag.location().getPath() : BuiltInRegistries.ITEM.getKey(items[0].asItem()).getPath();

        return prefix + "_" + suffix;
    }

}
