package com.favouriteless.enchanted.common.recipes.witch_oven;

import com.favouriteless.enchanted.core.init.EnchantedRecipeTypes;
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
        this.type = EnchantedRecipeTypes.WITCH_OVEN;
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
        return inv.getItem(0).sameItem(this.ingredient);
    }

    public boolean matches(ItemStack itemStack) {
        return ingredient.sameItem(itemStack);
    }

    @Override
    public ItemStack assemble(IInventory inv) {
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
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }
}
