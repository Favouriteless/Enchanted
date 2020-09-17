package com.favouriteless.magicraft.recipe.witch_oven;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class WitchOvenSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WitchOvenRecipe> {

    @Override
    public WitchOvenRecipe read(ResourceLocation recipeId, JsonObject json) {

        ItemStack itemIn = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, "ingredient"))));
        ItemStack itemOut = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, "result"))));

        int jarsNeeded = JSONUtils.getInt(json, "jarsneeded", 1);

        return new WitchOvenRecipe(recipeId, itemIn, itemOut, jarsNeeded);
    }

    @Nullable
    @Override
    public WitchOvenRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {

        ItemStack itemIn = buffer.readItemStack();
        ItemStack itemOut = buffer.readItemStack();
        int jarsNeeded = buffer.readInt();

        return new WitchOvenRecipe(recipeId, itemIn, itemOut, jarsNeeded);
    }

    @Override
    public void write(PacketBuffer buffer, WitchOvenRecipe recipe) {

        buffer.writeItemStack(recipe.getInput());
        buffer.writeItemStack(recipe.getOutput());
        buffer.writeInt(recipe.getJarsNeeded());

    }

}