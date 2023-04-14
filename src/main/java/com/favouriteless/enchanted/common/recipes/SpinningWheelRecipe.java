/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.recipes;


import com.favouriteless.enchanted.common.init.EnchantedRecipeTypes;
import com.favouriteless.enchanted.core.util.StaticItemStackHelper;
import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SpinningWheelRecipe implements Recipe<Container> {

    private final RecipeType<?> type;
    private final ResourceLocation id;

    private final NonNullList<ItemStack> itemsIn;
    private final ItemStack result;
    private final int power;

    public SpinningWheelRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, ItemStack result, int power) {
        this.type = EnchantedRecipeTypes.SPINNING_WHEEL;
        this.id = id;
        this.itemsIn = itemsIn;
        this.result = result;
        this.power = power;
    }

    @Override
    public boolean matches(Container inv, Level level) {
        ItemStack mainIn = inv.getItem(0);
        if(!mainIn.sameItem(itemsIn.get(0)) || mainIn.getCount() < itemsIn.get(0).getCount()) // If "main" input does not match
            return false;

        for(int i = 1; i < itemsIn.size(); i++) {
            ItemStack itemNeeded = itemsIn.get(i).copy();

            if(itemNeeded.sameItem(inv.getItem(1)))
                itemNeeded.shrink(inv.getItem(1).getCount());
            if(itemNeeded.sameItem(inv.getItem(2)))
                itemNeeded.shrink(inv.getItem(2).getCount());

            if(!itemNeeded.isEmpty()) // If not empty then there was not enough of this item
                return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(Container inv) {
        ItemStack stack = inv.getItem(3);

        if(StaticItemStackHelper.canStack(stack, result)) {
            stack.grow(result.getCount());
        }
        else {
            stack = result.copy();
        }
        inv.setItem(3, stack);
        return stack;
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
        return EnchantedRecipeTypes.SPINNING_WHEEL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return type;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public NonNullList<ItemStack> getItemsIn() {
        return itemsIn;
    }


    public int getPower() {
        return power;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SpinningWheelRecipe> {

        @Override
        public SpinningWheelRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(GsonHelper.getAsJsonArray(json, "ingredients"));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            int power = GsonHelper.getAsInt(json, "power", 0);

            return new SpinningWheelRecipe(recipeId, itemsIn, result, power);
        }

        @Nullable
        @Override
        public SpinningWheelRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int ingredientSize = buffer.readInt();
            NonNullList<ItemStack> itemsIn = NonNullList.create();
            for(int i = 0; i < ingredientSize; i++) {
                itemsIn.add(buffer.readItem());
            }

            ItemStack result = buffer.readItem();
            int power = buffer.readInt();

            return new SpinningWheelRecipe(recipeId, itemsIn, result, power);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SpinningWheelRecipe recipe) {
            NonNullList<ItemStack> items = recipe.getItemsIn();
            buffer.writeInt(items.size());
            items.forEach(buffer::writeItem);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.getPower());

        }

    }
}
