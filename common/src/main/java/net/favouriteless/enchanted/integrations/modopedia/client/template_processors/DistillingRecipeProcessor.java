package net.favouriteless.enchanted.integrations.modopedia.client.template_processors;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.favouriteless.enchanted.common.util.LangUtils;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.BookTexture;
import net.favouriteless.modopedia.api.book.BookTexture.Rectangle;
import net.favouriteless.modopedia.api.book.TemplateProcessor;
import net.favouriteless.modopedia.api.book.page_components.ItemDisplay;
import net.favouriteless.modopedia.api.registries.client.BookTextureRegistry;
import net.favouriteless.modopedia.client.page_components.item_displays.EmptyItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.GridItemDisplay;
import net.favouriteless.modopedia.client.page_components.item_displays.SimpleItemDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class DistillingRecipeProcessor implements TemplateProcessor {

    public static final ResourceLocation ID = Enchanted.id("distilling_recipe");

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        initComponents(book, lookup);
        initRecipes(lookup, level);
    }

    protected void initRecipes(MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(id);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(id + " is not a valid recipe.");
        }

        RecipeHolder<?> holder = optional.get();

        if (holder.value() instanceof DistillingRecipe recipe) {
            List<ItemDisplay> inputs = recipe.inputs().stream().<ItemDisplay>map(SimpleItemDisplay::new).toList();
            List<ItemDisplay> outputs = recipe.outputs().stream().<ItemDisplay>map(SimpleItemDisplay::new).toList();

            lookup.set("p_input1", Variable.of(inputs.getFirst()));
            lookup.set("p_input2", Variable.of(new GridItemDisplay(inputs.subList(1, inputs.size()), 2, 20, false)));
            lookup.set("p_output1", Variable.of(new GridItemDisplay(outputs.size() > 1 ? outputs.subList(0, 2) : outputs.subList(0, 1), 2, 19, false)));
            lookup.set("p_output2", Variable.of(outputs.size() > 2 ? new GridItemDisplay(outputs.subList(2, outputs.size()), 2, 19, false) : new EmptyItemDisplay()));
            lookup.set("p_power", Variable.of(Component.translatable(LangUtils.tooltip("altar_power"), recipe.power()).getString()));
        } else {
            throw new IllegalArgumentException("DistilleryRecipe template must use a DistilleryRecipe.");
        }
    }

    protected void initComponents(Book book, MutableLookup lookup) {
        BookTexture tex = BookTextureRegistry.get().getTexture(book.getTexture());
        if (tex == null) {
            throw new IllegalStateException("DistilleryRecipe must have a valid BookTexture");
        }

        Rectangle page = tex.pages().get(lookup.get("page_num").asInt() % tex.pages().size());
        Rectangle back = tex.widgets().get("distillery");
        if (back == null) {
            throw new IllegalStateException("DistilleryRecipe must have a valid distillery widget");
        }

        int center = page.width() / 2;

        lookup.set("p_x", Variable.of(center - back.width() / 2));
        lookup.set("p_input1_x", Variable.of(center - 8));
        lookup.set("p_input2_x", Variable.of(center - 18));
        lookup.set("p_output1_x", Variable.of(center - 37));
        lookup.set("p_output2_x", Variable.of(center + 2));
    }

}
