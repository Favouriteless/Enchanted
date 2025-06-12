package net.favouriteless.enchanted.integrations.modopedia.template_processors;

import net.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.books.Book;
import net.favouriteless.modopedia.api.books.TemplateProcessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class CauldronTypeRecipeProcessor implements TemplateProcessor {

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        RecipeHolder<?> holder = level.getRecipeManager().byKey(id).orElseThrow();

        if(holder.value() instanceof CauldronTypeRecipe recipe) {
            List<ItemStack[]> inputs = new ArrayList<>();
            recipe.getInputs().forEach(i -> inputs.add(new ItemStack[] { i }));

            int rows = inputs.size() / 5 + 1;

            lookup.set("inputs_y", Variable.of(35 - rows * 8)); // 31 is right in the center of the ingredients area
            lookup.set("inputs", Variable.of(inputs));
            lookup.set("output", Variable.of(List.<ItemStack[]>of(new ItemStack[] { recipe.getResultItem(level.registryAccess()) })));
            lookup.set("power", Variable.of(recipe.getPower() + " Altar Power"));
        }
        else {
            throw new IllegalArgumentException("CauldronTypeRecipe template must use a CauldronTypeRecipe recipe.");
        }
    }

}
