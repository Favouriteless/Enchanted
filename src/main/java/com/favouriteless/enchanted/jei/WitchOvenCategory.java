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


package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.recipes.WitchOvenRecipe;
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

public class WitchOvenCategory implements IRecipeCategory<WitchOvenRecipe> {

    IJeiHelpers helper;

    RecipeType<WitchOvenRecipe> type;

    IDrawableAnimated fire;
    IDrawableAnimated arrow;

    public WitchOvenCategory(IJeiHelpers helper, RecipeType<WitchOvenRecipe> type) {
        this.type = type;
        this.helper = helper;
        IDrawableStatic fire = helper.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven_craft.png"), 176, 0, 14, 14);
        this.fire = helper.getGuiHelper().createAnimatedDrawable(fire, 120, IDrawableAnimated.StartDirection.BOTTOM, false);
        IDrawableStatic arrow = helper.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven_craft.png"), 176, 14, 24, 17);
        this.arrow = helper.getGuiHelper().createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public Component getTitle() {
        return new TextComponent("witch_oven");
    }

    @Override
    public IDrawable getBackground() {
        int imageWidth = 176;
        int imageHeight = 85;
        return helper.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven_craft.png"), 0, 0, imageWidth, imageHeight);
    }

    @Override
    public IDrawable getIcon() {

        return helper.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.WITCH_OVEN.get()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WitchOvenRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 53, 17).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 17).addIngredient(VanillaTypes.ITEM_STACK, recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.CATALYST, 53, 53).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.CLAY_JAR.get(), recipe.getJarsNeeded()));
    }


    @Override
    public void draw(WitchOvenRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        fire.draw(stack, 80, 37);
        arrow.draw(stack, 76, 16);
    }

    @Override
    public ResourceLocation getUid() {
        return type.getUid();
    }

    @Override
    public Class<? extends WitchOvenRecipe> getRecipeClass() {
        return type.getRecipeClass();
    }

    @Override
    public RecipeType<WitchOvenRecipe> getRecipeType() {
        return this.type;
    }

}
