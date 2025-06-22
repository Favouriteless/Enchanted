package net.favouriteless.enchanted.api.datagen.builders.recipe;

import net.favouriteless.enchanted.common.recipes.CauldronTypeRecipe;
import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CauldronTypeRecipeBuilder extends ERecipeBuilder {

    private final CauldronTypeRecipeFactory<?> factory;
    private final List<ItemStack> inputs = new ArrayList<>();
    private final ItemStack result;
    private final int power;

    private int cookColour = 0x2D155E;
    private int finalColour = 0x4A1AAD;

    private CauldronTypeRecipeBuilder(String subfolder, CauldronTypeRecipeFactory<?> factory, ItemStack result, int power) {
        super(subfolder);
        this.power = power;
        this.result = result;
        this.factory = factory;
    }

    public static CauldronTypeRecipeBuilder cauldron(ItemStack result, int power) {
        return new CauldronTypeRecipeBuilder("witch_cauldron", WitchCauldronRecipe::new, result, power);
    }

    public static CauldronTypeRecipeBuilder kettle(ItemStack result, int power) {
        return new CauldronTypeRecipeBuilder("kettle", KettleRecipe::new, result, power);
    }

    public CauldronTypeRecipeBuilder inputs(ItemStack... inputs) {
        Collections.addAll(this.inputs, inputs);
        return this;
    }

    public CauldronTypeRecipeBuilder inputs(ItemLike... inputs) {
        for(ItemLike item : inputs)
            this.inputs.add(item.asItem().getDefaultInstance());

        return this;
    }

    public CauldronTypeRecipeBuilder cookColor(int colour) {
        cookColour = colour;
        return this;
    }

    public CauldronTypeRecipeBuilder finalColor(int colour) {
        finalColour = colour;
        return this;
    }

    @Override
    @NotNull
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(id, factory.create(inputs, result, power, cookColour, finalColour), null);
    }

    @FunctionalInterface
    public interface CauldronTypeRecipeFactory<T extends CauldronTypeRecipe> {

        T create(List<ItemStack> inputs, ItemStack result, int power, int cookColour, int finalColour);

    }

}
