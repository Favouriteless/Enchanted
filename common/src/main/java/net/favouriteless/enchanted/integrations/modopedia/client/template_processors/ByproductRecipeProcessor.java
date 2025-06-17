package net.favouriteless.enchanted.integrations.modopedia.client.template_processors;

import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.recipes.ByproductRecipe;
import net.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookTexture;
import net.favouriteless.modopedia.api.book.BookTexture.Rectangle;
import net.favouriteless.modopedia.api.book.TemplateProcessor;
import net.favouriteless.modopedia.api.registries.client.BookTextureRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ByproductRecipeProcessor implements TemplateProcessor {

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(id);
        if(optional.isEmpty())
            throw new IllegalArgumentException(id + " is not a valid recipe.");

        RecipeHolder<?> holder = optional.get();

        if(holder.value() instanceof ByproductRecipe recipe) {
            Ingredient input = recipe.getInput();

            List<ItemStack> inputs = new ArrayList<>();
            List<ItemStack> outputs = new ArrayList<>();

            for(ItemStack stack : input.getItems()) {
                List<RecipeHolder<SmeltingRecipe>> smeltingRecipes = level.getRecipeManager().getRecipesFor(RecipeType.SMELTING, new SingleRecipeInput(stack), level);
                for(RecipeHolder<SmeltingRecipe> sHolder : smeltingRecipes) {
                    ItemStack result = sHolder.value().getResultItem(level.registryAccess());

                    inputs.add(stack);
                    outputs.add(result);
                }
            }

            ItemStack result = recipe.getResultItem(level.registryAccess());

            lookup.set("inputs", Variable.of(List.<ItemStack[]>of(inputs.toArray(ItemStack[]::new))));
            lookup.set("outputs", Variable.of(List.<ItemStack[]>of(outputs.toArray(ItemStack[]::new))));
            lookup.set("byproduct", Variable.of(List.<ItemStack[]>of(new ItemStack[] { result })));
            lookup.set("jars", Variable.of(List.<ItemStack[]>of(new ItemStack[] { new ItemStack(EItems.CLAY_JAR.get(), result.getCount()) })));
        }
        else {
            throw new IllegalArgumentException("ByproductRecipe template must use a ByproductRecipe.");
        }
    }

}
