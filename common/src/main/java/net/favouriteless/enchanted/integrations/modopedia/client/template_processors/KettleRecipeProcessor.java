package net.favouriteless.enchanted.integrations.modopedia.client.template_processors;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.favouriteless.enchanted.common.util.LangUtils;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.TemplateProcessor;
import net.favouriteless.modopedia.api.book.page_components.ItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.GridItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.SimpleItemDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KettleRecipeProcessor implements TemplateProcessor {

    public static final ResourceLocation ID = Enchanted.id("kettle_recipe");

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(id + " is not a valid recipe.");
        }

        RecipeHolder<?> holder = optional.get();

        if (holder.value() instanceof KettleRecipe recipe) {
            List<List<ItemStack>> inputs = new ArrayList<>();
            recipe.getInputs().forEach(i -> inputs.add(List.of(i)));

            int rows = (int) Math.ceil(inputs.size() / 5.0F);

            lookup.set("p_inputs_y", Variable.of(35 - rows * 8)); // 35 is right in the center of the ingredients area
            lookup.set("p_inputs", Variable.of(new GridItemDisplay(recipe.getInputs().stream().<ItemDisplay>map(SimpleItemDisplay::new).toList(), 5, 16, true)));
            lookup.set("p_output", Variable.of(new SimpleItemDisplay(recipe.getResultItem(level.registryAccess()))));
            lookup.set("p_power", Variable.of(Component.translatable(LangUtils.tooltip("altar_power"), recipe.getPower()).getString()));
        } else {
            throw new IllegalArgumentException("KettleRecipe template must use a kettle recipe.");
        }
    }

}
