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
import com.favouriteless.enchanted.jei.recipes.JEIMutandisRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MutandisCategory implements IRecipeCategory<JEIMutandisRecipe> {

    private final IJeiHelpers jeiHelpers;
    private final RecipeType<JEIMutandisRecipe> recipeTypeRite;
    private final ItemStack icon;
    private final TranslatableComponent title;

    public MutandisCategory(IJeiHelpers jeiHelpers, RecipeType<JEIMutandisRecipe> recipeTypeRite, ItemStack icon, TranslatableComponent title) {
        this.jeiHelpers = jeiHelpers;
        this.recipeTypeRite = recipeTypeRite;
        this.icon=icon;
        this.title = title;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JEIMutandisRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,27,35).addItemStacks(recipe.getInputs());
        builder.addSlot(RecipeIngredientRole.OUTPUT,76,35).addItemStack(recipe.getOutput());
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/jei/mutandis.png"), 0, 0, 120, 68);
    }

    @Override
    public void draw(JEIMutandisRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawText(Minecraft.getInstance(),stack,recipe.getDescription(),120,10,0xFFFFFF);
    }

    private void drawText(Minecraft minecraft, PoseStack poseStack, String text, int x, int y, int mainColor) {
        int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
        int width = minecraft.font.width(text);
        if(width>150){
            drawText(minecraft,poseStack,text.substring(0,text.length()/2),x,y,mainColor);
            drawText(minecraft,poseStack,text.substring(text.length()/2,text.length()),x,y+10,mainColor);
            return;
        }
        int cx = x/2 - width/2 - 1;
        minecraft.font.draw(poseStack, text, cx + 1, y, shadowColor);
    }

    @Override
    public IDrawable getIcon() {
        return this.jeiHelpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, this.icon);
    }

    @Override
    public ResourceLocation getUid() {
        return this.recipeTypeRite.getUid();
    }

    @Override
    public Class<? extends JEIMutandisRecipe> getRecipeClass() {
        return this.recipeTypeRite.getRecipeClass();
    }
}
