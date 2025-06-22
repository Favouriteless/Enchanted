package net.favouriteless.enchanted.api.datagen.builders.recipe;

import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.Nullable;

public abstract class ERecipeBuilder implements RecipeBuilder {

    private final String subfolder;

    protected ERecipeBuilder(String subfolder) {
        this.subfolder = subfolder;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return null;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return null;
    }

    @Override
    public final void save(RecipeOutput output) {
        save(output, getDefaultName());
    }

    @Override
    public final void save(RecipeOutput output, String id) {
        save(output, Enchanted.id(subfolder + "/" + id));
    }

    protected String getDefaultName() {
        return BuiltInRegistries.ITEM.getKey(getResult()).getPath();
    }

}
