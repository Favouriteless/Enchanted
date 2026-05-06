package net.favouriteless.enchanted.common.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.util.EExtraCodecs;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class MortarRecipe implements Recipe<SingleRecipeInput> {

    public static final MapCodec<MortarRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            EExtraCodecs.HEX_INT.fieldOf("cook_colour").forGetter(recipe -> recipe.colour)
    ).apply(instance, MortarRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MortarRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
            ItemStack.STREAM_CODEC, r -> r.result,
            ByteBufCodecs.INT, r -> r.colour,
            MortarRecipe::new
    );

    protected final Ingredient ingredient;
    protected final ItemStack result;

    protected final int colour;

    public MortarRecipe(Ingredient ingredient, ItemStack result, int colour) {
        super();
        this.ingredient = ingredient;
        this.result = result;
        this.colour = colour;
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
    public ItemStack getResultItem(Provider registries) {
        return result;
    }


    public int getColour() {
        return colour;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeTypes.MORTAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.MORTAR.get();
    }

}
