package com.favouriteless.enchanted.common.recipes.distillery;

import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class DistillerySerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DistilleryRecipe> {

    @Override
    public DistilleryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

        NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemList(JSONUtils.getAsJsonArray(json, "iteminputs"));
        NonNullList<ItemStack> itemsOut = StaticJSONHelper.readItemList(JSONUtils.getAsJsonArray(json, "itemoutputs"));
        int cookTime = JSONUtils.getAsInt(json, "cookTime", 200);

        return new DistilleryRecipe(recipeId, itemsIn, itemsOut, cookTime);
    }

    @Nullable
    @Override
    public DistilleryRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {

        int inSize = buffer.readInt();
        NonNullList<ItemStack> itemsIn = NonNullList.create();
        for (int x = 0; x < inSize; ++x) {
            itemsIn.add(buffer.readItem());
        }

        int outSize = buffer.readInt();
        NonNullList<ItemStack> itemsOut = NonNullList.create();
        for (int x = 0; x < outSize; ++x) {
            itemsIn.add(buffer.readItem());
        }

        int cookTime = buffer.readInt();

        return new DistilleryRecipe(recipeId, itemsIn, itemsOut, cookTime);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, DistilleryRecipe recipe) {

        buffer.writeInt(recipe.getItemsIn().size());
        for (ItemStack stack : recipe.getItemsIn()) {
            buffer.writeItem(stack);
        }

        buffer.writeInt(recipe.getItemsOut().size());
        for (ItemStack stack : recipe.getItemsOut()) {
            buffer.writeItem(stack);
        }

        buffer.writeInt(recipe.getCookTime());

    }

}