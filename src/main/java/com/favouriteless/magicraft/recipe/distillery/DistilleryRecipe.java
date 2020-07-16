package com.favouriteless.magicraft.recipe.distillery;

import com.favouriteless.magicraft.init.MagicraftRecipeTypes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

public class DistilleryRecipe implements IRecipe<IInventory> {

    protected final IRecipeType<?> type;
    protected final ResourceLocation id;

    private final NonNullList<ItemStack> itemsIn;
    private final NonNullList<ItemStack> itemsOut;
    private final int cookTime;


    public DistilleryRecipe(ResourceLocation id, NonNullList<ItemStack> itemsIn, NonNullList<ItemStack> itemsOut, int cookTime) {
        this.type = MagicraftRecipeTypes.DISTILLERY;
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
                    if(item.getItem() == stack.getItem() && item.getCount() >= stack.getCount()) {
                        requiredItems--;
                        break;
                    }
                }
            }
            return requiredItems == 0;
        }
        return false;
    }



    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
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
