/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.recipes;

import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;

public class DistilleryRecipe implements Recipe<Container> {

    protected final RecipeType<?> type;
    protected final ResourceLocation id;

    private final NonNullList<ItemStack> itemsIn;
    private final NonNullList<ItemStack> itemsOut;
    private final int cookTime;


    public DistilleryRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, NonNullList<ItemStack> itemsOut, int cookTime) {
        this.type = EnchantedRecipeTypes.DISTILLERY;
        this.id = id;

        this.itemsIn = itemsIn;
        this.itemsOut = itemsOut;
        this.cookTime = cookTime;
    }

    public NonNullList<ItemStack> getItemsOut() {
        return this.itemsOut;
    }

    public NonNullList<ItemStack> getItemsIn() {
        return this.itemsIn;
    }

    public int getCookTime() {
        return cookTime;
    }

    public boolean matches(List<ItemStack> items) {
        if(!items.isEmpty()) {
            int requiredItems = getItemsIn().size();

            for(ItemStack stack : getItemsIn()) {
                for(ItemStack item : items) {
                    if(stack.getItem() == item.getItem() && item.getCount() >= stack.getCount()) {
                        requiredItems--;
                        break;
                    }
                }
            }
            return requiredItems == 0;
        }
        return false;
    }

    public int getJarCount() {
        int count = 0;

        for(ItemStack stack : itemsOut) {
            count += stack.getCount();
        }

        return count;
    }



    @Override
    public boolean matches(Container inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(Container inv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.DISTILLERY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }



    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DistilleryRecipe> {

        @Override
        public DistilleryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {

            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "iteminputs"));
            NonNullList<ItemStack> itemsOut = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "itemoutputs"));
            int cookTime = GsonHelper.getAsInt(json, "cookTime", 200);

            return new DistilleryRecipe(recipeId, itemsIn, itemsOut, cookTime);
        }

        @Nullable
        @Override
        public DistilleryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {

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
        public void toNetwork(FriendlyByteBuf buffer, DistilleryRecipe recipe) {

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
}
