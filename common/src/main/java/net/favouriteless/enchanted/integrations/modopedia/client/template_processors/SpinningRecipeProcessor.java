package net.favouriteless.enchanted.integrations.modopedia.client.template_processors;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.SpinningRecipe;
import net.favouriteless.enchanted.common.util.LangUtils;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.TemplateProcessor;
import net.favouriteless.modopedia.client.page_components.item_displays.SimpleItemDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class SpinningRecipeProcessor implements TemplateProcessor {

    public static final ResourceLocation ID = Enchanted.id("spinning_recipe");

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(id);
        if(optional.isEmpty())
            throw new IllegalArgumentException(id + " is not a valid recipe.");

        RecipeHolder<?> holder = optional.get();

        if(holder.value() instanceof SpinningRecipe recipe) {
            List<ItemStack> inputs = recipe.getInputs();

            lookup.set("p_input1", Variable.of(new SimpleItemDisplay(inputs.get(0))));
            lookup.set("p_input2", Variable.of(new SimpleItemDisplay(inputs.get(1))));
            lookup.set("p_input3", Variable.of(new SimpleItemDisplay(inputs.get(2))));
            lookup.set("p_output", Variable.of(new SimpleItemDisplay(recipe.getResultItem(level.registryAccess()))));
            lookup.set("p_power", Variable.of(Component.translatable(LangUtils.tooltip("altar_power"), recipe.getPower()).getString()));
        }
        else {
            throw new IllegalArgumentException("SpinningRecipe template must use a SpinningRecipe recipe.");
        }
    }

}
