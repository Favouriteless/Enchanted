package com.favouriteless.magicraft.recipe.witch_oven;

import com.favouriteless.magicraft.init.MagicraftRecipeTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WitchOvenRecipe implements IRecipe<IInventory> {

    private IRecipeType<?> type;
    private ResourceLocation id;

    private ItemStack ingredient;
    private ItemStack result;
    private int jarsNeeded;

    public WitchOvenRecipe(ResourceLocation id, ItemStack ingredient, ItemStack result, int jarsNeeded) {
        this.type = MagicraftRecipeTypes.WITCH_OVEN;
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.jarsNeeded = jarsNeeded;
    }

    public int getJarsNeeded() {
        return this.jarsNeeded;
    }

    public ItemStack getInput() {
        return this.ingredient;
    }

    public ItemStack getOutput() {
        return this.result;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    public boolean matches(ItemStack itemStack) {
        return ingredient.isItemEqual(itemStack);
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return null;
    }

    @Override
    public boolean canFit(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }
}
