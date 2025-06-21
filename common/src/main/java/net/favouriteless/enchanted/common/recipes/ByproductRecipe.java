package net.favouriteless.enchanted.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ByproductRecipe implements Recipe<SingleRecipeInput> {

    public static final MapCodec<ByproductRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group (
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
    ).apply(instance, ByproductRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ByproductRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient,
            ItemStack.STREAM_CODEC, recipe -> recipe.result,
            ByproductRecipe::new
    );

    private final Ingredient ingredient;
    private final ItemStack result;

    public ByproductRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(Provider registries) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    public Ingredient getInput() {
        return this.ingredient;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BYPRODUCT.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeTypes.BYPRODUCT_SERIALIZER.get();
    }

}
