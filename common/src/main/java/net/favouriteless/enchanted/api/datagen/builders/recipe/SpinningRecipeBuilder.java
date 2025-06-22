package net.favouriteless.enchanted.api.datagen.builders.recipe;

import net.favouriteless.enchanted.common.recipes.SpinningRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public class SpinningRecipeBuilder extends ERecipeBuilder {

    private final List<ItemStack> inputs;
    private final ItemStack result;

    private int power = 0;
    private int duration = 300;

    private SpinningRecipeBuilder(ItemLike result, ItemStack... inputs) {
        super("spinning");
        this.result = new ItemStack(result.asItem());
        this.inputs = List.of(inputs);

        if(inputs.length > 3)
            throw new IllegalArgumentException("Tried to create spinning recipe with more than 3 inputs.");
    }

    public static SpinningRecipeBuilder create(ItemLike result, ItemLike... items) {
        ItemStack[] inputs = new ItemStack[items.length];
        for(int i = 0; i < items.length; i++)
            inputs[i] = items[i].asItem().getDefaultInstance();

        return new SpinningRecipeBuilder(result, inputs);
    }

    public static SpinningRecipeBuilder create(ItemLike result, ItemStack... items) {
        return new SpinningRecipeBuilder(result, items);
    }

    public SpinningRecipeBuilder power(int power) {
        this.power = power;
        return this;
    }

    public SpinningRecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(id, new SpinningRecipe(inputs, result, power, duration), null);
    }

}
