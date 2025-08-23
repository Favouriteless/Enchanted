package net.favouriteless.enchanted.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.favouriteless.enchanted.common.util.EExtraCodecs;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class KettleRecipe implements Recipe<ListInput> {

    public static final MapCodec<KettleRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            Codec.INT.optionalFieldOf("power", 0).forGetter(recipe -> recipe.power),
            EExtraCodecs.HEX_INT.optionalFieldOf("cook_colour", 0x2D155E).forGetter(recipe -> recipe.cookColour),
            EExtraCodecs.HEX_INT.optionalFieldOf("final_colour", 0x4A1AAD).forGetter(recipe -> recipe.finalColour)
    ).apply(instance, KettleRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KettleRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, recipe -> recipe.inputs,
            ItemStack.STREAM_CODEC, recipe -> recipe.result,
            ByteBufCodecs.INT, recipe -> recipe.power,
            ByteBufCodecs.INT, recipe -> recipe.cookColour,
            ByteBufCodecs.INT, recipe -> recipe.finalColour,
            KettleRecipe::new
    );

    protected final List<ItemStack> inputs;
    protected final ItemStack result;
    protected final int power;

    protected final int cookColour;
    protected final int finalColour;

    public KettleRecipe(List<ItemStack> inputs, ItemStack result, int power, int cookColour, int finalColour) {
        super();
        this.inputs = inputs;
        this.result = result;
        this.power = power;
        this.cookColour = cookColour;
        this.finalColour = finalColour;
    }

    @Override
    public boolean matches(ListInput input, Level level) {
        return !input.isEmpty() && input.size() <= inputs.size() && itemsMatch(input);
    }

    public boolean fullMatch(ListInput input) {
        return input.size() == inputs.size() && itemsMatch(input);
    }

    public boolean itemsMatch(ListInput input) {
        for(int i = 0; i < inputs.size() && i < input.size(); i++) {
            if(!ItemUtils.isSameItemPartial(inputs.get(i), input.getItem(i)))
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(ListInput input, Provider registries) {
        return result.copy();
    }

    @Override
    public ItemStack getResultItem(Provider registries) {
        return result;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public int getPower() {
        return power;
    }

    public int getCookColour() {
        return cookColour;
    }

    public int getFinalColour() {
        return finalColour;
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
        return ERecipeTypes.KETTLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.KETTLE.get();
    }

}
