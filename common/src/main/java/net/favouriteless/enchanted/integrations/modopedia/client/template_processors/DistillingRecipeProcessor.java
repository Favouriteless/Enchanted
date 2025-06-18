package net.favouriteless.enchanted.integrations.modopedia.client.template_processors;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.TemplateProcessor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DistillingRecipeProcessor implements TemplateProcessor {

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(id);
        if(optional.isEmpty())
            throw new IllegalArgumentException(id + " is not a valid recipe.");

        RecipeHolder<?> holder = optional.get();

        if(holder.value() instanceof DistillingRecipe recipe) {
            List<List<ItemStack>> inputs = recipe.getInputs().stream().map(List::of).toList();
            List<List<ItemStack>> outputs = recipe.getOutputs().stream().map(List::of).toList();

            lookup.set("firstInput", Variable.of(inputs.subList(0, 1)));
            lookup.set("inputs", Variable.of(inputs.subList(1, inputs.size())));
            lookup.set("outputs", Variable.of(outputs));
            lookup.set("power", Variable.of(Component.translatable(Enchanted.translationKey("tooltip", "altar_power"), recipe.getPower()).getString()));
        }
        else {
            throw new IllegalArgumentException("DistilleryRecipe template must use a DistilleryRecipe.");
        }
    }

}
