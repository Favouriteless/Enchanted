package net.favouriteless.enchanted.neoforge.datagen.builders.recipe;

import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistillingRecipeBuilder extends ERecipeBuilder {

    private final List<ItemStack> inputs;
    private final List<ItemStack> results = new ArrayList<>();
    private int duration = 300;
    private int power = 750;

    private DistillingRecipeBuilder(List<ItemStack> inputs) {
        super("distilling");
        this.inputs = inputs;
    }

    public static DistillingRecipeBuilder create(ItemStack... inputs) {
        if(inputs.length > 3)
            throw new IllegalStateException("Distillery recipes cannot have more than 3 inputs.");
        return new DistillingRecipeBuilder(List.of(inputs));
    }

    public static DistillingRecipeBuilder create(ItemLike... inputs) {
        if(inputs.length > 3)
            throw new IllegalStateException("Distillery recipes cannot have more than 3 inputs.");

        List<ItemStack> stacks = new ArrayList<>();
        for(ItemLike input : inputs)
            stacks.add(new ItemStack(input));

        return new DistillingRecipeBuilder(stacks);
    }

    public DistillingRecipeBuilder results(ItemStack... results) {
        Collections.addAll(this.results, results);
        if(this.results.size() > 4)
            throw new IllegalStateException("Distillery recipes cannot have more than 4 mutagenSets.");
        return this;
    }

    public DistillingRecipeBuilder results(ItemLike... results) {
        ItemStack[] stacks = new ItemStack[results.length];
        for(int i = 0; i < results.length; i++)
            stacks[i] = new ItemStack(results[i]);

        Collections.addAll(this.results, stacks);
        if(this.results.size() > 4)
            throw new IllegalStateException("Distillery recipes cannot have more than 4 mutagenSets.");
        return this;
    }

    public DistillingRecipeBuilder duration(int duration) {
        if(duration < 1)
            throw new IllegalArgumentException("Distillery recipes cannot have a duration smaller than 1.");
        this.duration = duration;
        return this;
    }

    public DistillingRecipeBuilder power(int power) {
        if(power < 0)
            throw new IllegalArgumentException("Distillery recipes cannot have a power smaller than 0.");
        this.power = power;
        return this;
    }

    @Override
    @NotNull
    public Item getResult() {
        return results.get(0).getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(id, new DistillingRecipe(inputs, results, duration, power), null);
    }

    @Override
    protected String getDefaultName() {
        StringBuilder name = new StringBuilder();
        for(ItemStack item : inputs)
            if(item.getItem() != EItems.CLAY_JAR.get())
                name.append(BuiltInRegistries.ITEM.getKey(item.getItem()).getPath()).append("_");
        name.deleteCharAt(name.length() - 1); // Delete trailing _

        return name.toString();
    }

}
