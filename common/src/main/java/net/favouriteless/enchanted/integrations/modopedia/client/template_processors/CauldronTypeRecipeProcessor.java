package net.favouriteless.enchanted.integrations.modopedia.client.template_processors;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import net.favouriteless.modopedia.api.Lookup.MutableLookup;
import net.favouriteless.modopedia.api.Variable;
import net.favouriteless.modopedia.api.book.Book;
import net.favouriteless.modopedia.api.book.TemplateProcessor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CauldronTypeRecipeProcessor implements TemplateProcessor {

    public static final ResourceLocation ID = Enchanted.id("cauldron_type_recipe");

    @Override
    public void init(Book book, MutableLookup lookup, Level level) {
        ResourceLocation id = lookup.get("recipe").as(ResourceLocation.class);

        Optional<RecipeHolder<?>> optional = level.getRecipeManager().byKey(id);
        if(optional.isEmpty())
            throw new IllegalArgumentException(id + " is not a valid recipe.");

        RecipeHolder<?> holder = optional.get();

        if(holder.value() instanceof CauldronTypeRecipe recipe) {
            List<List<ItemStack>> inputs = new ArrayList<>();
            recipe.getInputs().forEach(i -> inputs.add(List.of(i)));

            int rows = inputs.size() / 5 + 1;

            lookup.set("inputs_y", Variable.of(35 - rows * 8)); // 31 is right in the center of the ingredients area
            lookup.set("inputs", Variable.of(inputs));
            lookup.set("output", Variable.of(List.of(List.of(recipe.getResultItem(level.registryAccess())))));
            lookup.set("power", Variable.of(Component.translatable(Enchanted.translationKey("tooltip", "altar_power"), recipe.getPower()).getString()));
        }
        else {
            throw new IllegalArgumentException("CauldronTypeRecipe template must use a CauldronTypeRecipe recipe.");
        }
    }

}
