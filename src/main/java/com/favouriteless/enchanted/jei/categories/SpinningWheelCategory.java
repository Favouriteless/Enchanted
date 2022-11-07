/*
 *
 *   Copyright (c) 2022. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.jei.categories;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpinningWheelCategory implements IRecipeCategory<SpinningWheelRecipe> {

    IJeiHelpers jeiHelpers;
    RecipeType<SpinningWheelRecipe> type;

    IDrawableAnimated leftArrow;
    IDrawableAnimated rightArrow;

    public SpinningWheelCategory(IJeiHelpers jeiHelpers, RecipeType<SpinningWheelRecipe> recipeTypeSpinningWheel) {
        this.jeiHelpers = jeiHelpers;
        this.type = recipeTypeSpinningWheel;
        IDrawableStatic leftArrow = jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/spinning_wheel.png"), 176, 0, 15, 20);
        this.leftArrow = jeiHelpers.getGuiHelper().createAnimatedDrawable(leftArrow, 120, IDrawableAnimated.StartDirection.BOTTOM, false);
        IDrawableStatic rightArrow = jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/spinning_wheel.png"), 176, 20, 15, 20);
        this.rightArrow = jeiHelpers.getGuiHelper().createAnimatedDrawable(rightArrow, 120, IDrawableAnimated.StartDirection.BOTTOM, false);
    }

    @Override
    public void draw(SpinningWheelRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.leftArrow.draw(stack,7,16);
        this.rightArrow.draw(stack,44,16);
    }

    @Override
    public Component getTitle() {
        return new TextComponent("Spinning Wheel");
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/spinning_wheel.png"), 20, 10, 140, 60);
    }

    @Override
    public IDrawable getIcon() {
        return jeiHelpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.SPINNING_WHEEL.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpinningWheelRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> inputs = recipe.getItemsIn();
        int[][] positions = new int[][]{{25, 13}, {13, 37}, {37, 37}};
        int pos = 0;
        for (ItemStack i : inputs) {
            builder.addSlot(RecipeIngredientRole.INPUT, positions[pos][0], positions[pos][1]).addIngredient(VanillaTypes.ITEM_STACK, i);
            pos++;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 25).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
    }

    @Override
    public ResourceLocation getUid() {
        return type.getUid();
    }

    @Override
    public Class<? extends SpinningWheelRecipe> getRecipeClass() {
        return type.getRecipeClass();
    }
}
