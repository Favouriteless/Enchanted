package net.favouriteless.enchanted.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.favouriteless.enchanted.common.init.ERecipeTypes;
import net.favouriteless.enchanted.common.util.EExtraCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class KettleRecipe extends CauldronTypeRecipe {

    public static final MapCodec<KettleRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.inputs),
            ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            Codec.INT.optionalFieldOf("power", 0).forGetter(recipe -> recipe.power),
            EExtraCodecs.HEX_INT.optionalFieldOf("cook_colour", 0x2D155E).forGetter(recipe -> recipe.cookColor),
            EExtraCodecs.HEX_INT.optionalFieldOf("final_colour", 0x4A1AAD).forGetter(recipe -> recipe.finalColor)
    ).apply(instance, KettleRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KettleRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStack.LIST_STREAM_CODEC, recipe -> recipe.inputs,
            ItemStack.STREAM_CODEC, recipe -> recipe.result,
            ByteBufCodecs.INT, recipe -> recipe.power,
            ByteBufCodecs.INT, recipe -> recipe.cookColor,
            ByteBufCodecs.INT, recipe -> recipe.finalColor,
            KettleRecipe::new
    );

    public KettleRecipe(List<ItemStack> inputs, ItemStack result, int power, int cookingColour, int finalColour) {
        super(inputs, result, power, cookingColour, finalColour);
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
