package net.favouriteless.enchanted.common.recipes;

import net.favouriteless.enchanted.common.recipes.recipe_inputs.ListInput;
import net.favouriteless.enchanted.common.util.ItemUtils;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class CauldronTypeRecipe implements Recipe<ListInput> {

    protected final List<ItemStack> inputs;
    protected final ItemStack result;
    protected final int power;

    protected final int cookColor;
    protected final int finalColor;

    public CauldronTypeRecipe(List<ItemStack> inputs, ItemStack result, int power, int cookColor, int finalColor) {
        this.inputs = inputs;
        this.result = result;
        this.power = power;

        this.cookColor = cookColor;
        this.finalColor = finalColor;
    }

    /**
     * @return true if inventory is a partial match for this recipe
     */
    @Override
    public boolean matches(ListInput input, Level level) {
        if(input.isEmpty() || input.size() > inputs.size())
            return false; // Too many items

        for(int i = 0; i < inputs.size() && i < input.size(); i++) {
            if(!ItemUtils.isSameItemPartial(inputs.get(i), input.getItem(i)))
                return false;
        }
        return true;
    }

    /**
     * @return true if inventory is a full match to this recipe
     */
    public boolean fullMatch(ListInput input) {
        if(input.size() != inputs.size()) // Same number of items
            return false;

        for(int i = 0; i < inputs.size(); i++) {
            if(!ItemUtils.isSameItemPartial(inputs.get(i), input.getItem(i)))
                return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(ListInput input, Provider registries) {
        return result.copy();
    }

    @Override
    public ItemStack getResultItem(Provider registries) {
        return result;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public int getPower() {
        return power;
    }

    public int getCookColour() {
        return cookColor;
    }

    public int getFinalColour() {
        return finalColor;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
}
