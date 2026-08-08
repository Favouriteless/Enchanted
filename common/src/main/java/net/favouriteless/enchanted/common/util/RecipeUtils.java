package net.favouriteless.enchanted.common.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

public class RecipeUtils {

    /**
     * Attempts to grab the result of a {@link Recipe} without registry access, using the client's level.
     *
     * @return The {@link ItemStack} result of recipe
     */
    public static ItemStack getResultItem(Recipe<?> recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        return recipe.getResultItem(level.registryAccess());
    }

    public static ItemStack getResultItem(RecipeHolder<?> recipe) {
        return getResultItem(recipe.value());
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<T> mapHolders(List<RecipeHolder<T>> holders) {
        return holders.stream()
                      .map(RecipeHolder::value)
                      .toList();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getHolders(RecipeType<T> type) {
        return Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(type);
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<T> getRecipes(RecipeType<T> type) {
        return mapHolders(getHolders(type));
    }

}