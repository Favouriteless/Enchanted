package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class WitchOvenRecipe implements Recipe<Container> {

    private final RecipeType<?> type;
    private final ResourceLocation id;

    private final Ingredient ingredient;
    private final ItemStack result;

    public WitchOvenRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result) {
        this.type = EnchantedRecipeTypes.WITCH_OVEN;
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
    }

    public Ingredient getInput() {
        return this.ingredient;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container inv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 4;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.WITCH_OVEN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }


    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<WitchOvenRecipe> {

        @Override
        public WitchOvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), false);

            return new WitchOvenRecipe(recipeId, ingredient, result);
        }

        @Nullable
        @Override
        public WitchOvenRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();

            return new WitchOvenRecipe(recipeId, ingredient, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WitchOvenRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItemStack(recipe.getResultItem(), true);
        }

    }
}
