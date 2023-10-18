package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class WitchCauldronRecipe extends CauldronTypeRecipe {

    public WitchCauldronRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, ItemStack itemOut, int power, int[] cookingColour, int[] finalColour) {
        super(EnchantedRecipeTypes.WITCH_CAULDRON, id, itemsIn, itemOut, power, cookingColour, finalColour);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.WITCH_CAULDRON_SERIALIZER.get();
    }



    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<WitchCauldronRecipe> {

        @Override
        public WitchCauldronRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "ingredients"));
            ItemStack itemOut = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            int power = GsonHelper.getAsInt(json, "power");
            int[] cookingColour = StaticJSONHelper.deserializeColour(GsonHelper.getAsJsonObject(json, "cookingColour"));
            int[] finalColour = StaticJSONHelper.deserializeColour(GsonHelper.getAsJsonObject(json, "finalColour"));

            return new WitchCauldronRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
        }

        @Nullable
        @Override
        public WitchCauldronRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

            int inSize = buffer.readInt();
            NonNullList<ItemStack> itemsIn = NonNullList.create();
            for (int x = 0; x < inSize; ++x) {
                itemsIn.add(buffer.readItem());
            }
            ItemStack itemOut = buffer.readItem();
            int power = buffer.readInt();
            int[] cookingColour = new int[] {(int)buffer.readShort(), (int)buffer.readShort(), (int)buffer.readShort() };
            int[] finalColour = new int[] {(int)buffer.readShort(), (int)buffer.readShort(), (int)buffer.readShort() };

            return new WitchCauldronRecipe(recipeId, itemsIn, itemOut, power, cookingColour, finalColour);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WitchCauldronRecipe recipe) {

            buffer.writeInt(recipe.getItemsIn().size());
            for (ItemStack item : recipe.getItemsIn()) {
                buffer.writeItem(item);
            }
            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.getPower());
            buffer.writeShort(recipe.getCookingRed());
            buffer.writeShort(recipe.getCookingGreen());
            buffer.writeShort(recipe.getCookingBlue());
            buffer.writeShort(recipe.getFinalRed());
            buffer.writeShort(recipe.getFinalGreen());
            buffer.writeShort(recipe.getFinalBlue());

        }

    }
}
