package net.favouriteless.enchanted.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
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

import java.util.ArrayList;
import java.util.List;

public record DistillingRecipe(List<ItemStack> inputs, List<ItemStack> outputs, int duration,
                               int power) implements Recipe<ListInput> {

    public static final MapCodec<DistillingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.sizeLimitedListOf(3).fieldOf("ingredients").forGetter(recipe -> recipe.inputs),
            ItemStack.CODEC.sizeLimitedListOf(4).fieldOf("results").forGetter(recipe -> recipe.outputs),
            Codec.INT.optionalFieldOf("duration", 300).forGetter(recipe -> recipe.duration),
            Codec.INT.optionalFieldOf("power", 750).forGetter(recipe -> recipe.power)
    ).apply(instance, DistillingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, DistillingRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, recipe -> recipe.inputs,
            ItemStack.LIST_STREAM_CODEC, recipe -> recipe.outputs,
            ByteBufCodecs.INT, recipe -> recipe.duration,
            ByteBufCodecs.INT, recipe -> recipe.power,
            DistillingRecipe::new
    );

    @Override
    public List<ItemStack> outputs() {
        List<ItemStack> out = new ArrayList<>();
        for (ItemStack stack : outputs) {
            out.add(stack.copy());
        }
        return out;
    }

    @Override
    public boolean matches(ListInput inv, Level level) {
        int requiredItems = inputs().size();

        for (ItemStack stack : inputs()) {
            for (int i = 0; i < 3; i++) {
                ItemStack item = inv.getItem(i);
                if (ItemUtils.isSameItemPartial(item, stack) && item.getCount() >= stack.getCount()) {
                    requiredItems--;
                    break;
                }
            }
        }
        return requiredItems == 0;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Deprecated
    @Override
    public ItemStack assemble(ListInput input, Provider registries) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    public ItemStack getResultItem(Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeTypes.DISTILLING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.DISTILLING.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

}
