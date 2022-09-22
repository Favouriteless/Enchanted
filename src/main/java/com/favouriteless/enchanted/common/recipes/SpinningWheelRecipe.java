/*
 * Copyright (c) 2021. Favouriteless
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
import com.favouriteless.enchanted.common.tileentity.SpinningWheelTileEntity;
import com.favouriteless.enchanted.core.StaticItemStackHelper;
import com.favouriteless.enchanted.core.util.StaticJSONHelper;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SpinningWheelRecipe implements IRecipe<SpinningWheelTileEntity> {

    private final IRecipeType<?> type;
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
    public boolean matches(SpinningWheelTileEntity inv, World worldIn) {
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
    public ItemStack assemble(SpinningWheelTileEntity inv) {
        ItemStack stack = inv.getItem(inv.getContainerSize()-1);

        if(StaticItemStackHelper.canStack(stack, result)) {
            stack.grow(result.getCount());
        }
        else {
            stack = result.copy();
        }
        inv.setItem(inv.getContainerSize()-1, stack);
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
    public IRecipeSerializer<?> getSerializer() {
        return EnchantedRecipeTypes.SPINNING_WHEEL_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
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

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpinningWheelRecipe> {

        @Override
        public SpinningWheelRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<ItemStack> itemsIn = StaticJSONHelper.readItemStackList(JSONUtils.getAsJsonArray(json, "inputs"));
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
            int power = JSONUtils.getAsInt(json, "power", 0);

            return new SpinningWheelRecipe(recipeId, itemsIn, result, power);
        }

        @Nullable
        @Override
        public SpinningWheelRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
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
        public void toNetwork(PacketBuffer buffer, SpinningWheelRecipe recipe) {
            NonNullList<ItemStack> items = recipe.getItemsIn();
            buffer.writeInt(items.size());
            items.forEach(buffer::writeItem);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeInt(recipe.getPower());

        }

    }
}
