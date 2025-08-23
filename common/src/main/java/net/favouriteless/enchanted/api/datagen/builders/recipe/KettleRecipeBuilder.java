package net.favouriteless.enchanted.api.datagen.builders.recipe;

import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KettleRecipeBuilder {

    public static InputStage of(ItemStack result, int power) {
        return new Builder(result, power);
    }

    public interface InputStage {

        Builder inputs(ItemStack... inputs);

        Builder inputs(ItemLike... inputs);

    }

    public static class Builder extends ERecipeBuilder implements InputStage {

        private final List<ItemStack> inputs = new ArrayList<>();
        private final ItemStack result;
        private final int power;

        private int cookColour = 0x2D155E;
        private int finalColour = 0x4A1AAD;

        private Builder(ItemStack result, int power) {
            super("kettle");
            this.power = power;
            this.result = result;
        }

        public Builder inputs(ItemStack... inputs) {
            Collections.addAll(this.inputs, inputs);
            return this;
        }

        public Builder inputs(ItemLike... inputs) {
            for(ItemLike item : inputs)
                this.inputs.add(item.asItem().getDefaultInstance());

            return this;
        }

        public Builder cookColor(int colour) {
            cookColour = colour;
            return this;
        }

        public Builder finalColor(int colour) {
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
            output.accept(id, new KettleRecipe(inputs, result, power, cookColour, finalColour), null);
        }

    }

}
