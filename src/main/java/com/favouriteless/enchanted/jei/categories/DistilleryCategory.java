/*
 *
 *   Copyright (c) 2023. Favouriteless
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
import com.favouriteless.enchanted.common.recipes.DistilleryRecipe;
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
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DistilleryCategory implements IRecipeCategory<DistilleryRecipe> {

    private final RecipeType<DistilleryRecipe> type;
    private final IJeiHelpers helper;

    private final IDrawableAnimated bubbles;
    private final IDrawableAnimated arrow;


    public DistilleryCategory(IJeiHelpers helper, RecipeType<DistilleryRecipe> type) {
        this.helper = helper;
        this.type = type;
        IDrawableStatic bubbles = helper.getGuiHelper().createDrawable(Enchanted.location("textures/gui/distillery.png"), 176, 0, 12, 29);
        this.bubbles = helper.getGuiHelper().createAnimatedDrawable(bubbles, 120, IDrawableAnimated.StartDirection.BOTTOM, false);
        IDrawableStatic arrow = helper.getGuiHelper().createDrawable(Enchanted.location("textures/gui/distillery.png"), 176, 29, 57, 61);
        this.arrow = helper.getGuiHelper().createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void draw(DistilleryRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.bubbles.draw(stack, 88, 22);
        this.arrow.draw(stack, 65, 8);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DistilleryRecipe recipe, IFocusGroup focuses) {
        int offset = 0;
        for (ItemStack i : recipe.getItemsIn()) {
            if (offset != 0)
                builder.addSlot(RecipeIngredientRole.INPUT, 50, offset).addIngredient(VanillaTypes.ITEM_STACK, i);
            offset += 20;
        }
        offset = 0;
        for (ItemStack i : recipe.getItemsOut()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 2 + offset).addIngredient(VanillaTypes.ITEM_STACK, i);
            offset += 19;
        }
        builder.addSlot(RecipeIngredientRole.CATALYST, 28, 30).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.CLAY_JAR.get(), recipe.getJarCount()));
        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("container.enchanted.distillery");
    }

    @Override
    public IDrawable getBackground() {
        return this.helper.getGuiHelper().createDrawable(Enchanted.location("textures/gui/distillery.png"), 4, 5, 146, 75);
    }

    @Override
    public IDrawable getIcon() {
        return helper.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.DISTILLERY.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return this.type.getUid();
    }

    @Override
    public Class<? extends DistilleryRecipe> getRecipeClass() {
        return this.type.getRecipeClass();
    }
}
