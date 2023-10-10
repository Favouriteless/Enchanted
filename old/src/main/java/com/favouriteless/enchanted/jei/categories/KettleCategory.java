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
import com.favouriteless.enchanted.common.recipes.KettleRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class KettleCategory implements IRecipeCategory<KettleRecipe> {

    private final RecipeType<KettleRecipe> type;
    private final IJeiHelpers jeiHelpers;

    private final int GUI_WIDTH = 140;
    private final IDrawableAnimated arrow;

    public KettleCategory(IJeiHelpers jeiHelpers, RecipeType<KettleRecipe> type) {
        this.jeiHelpers = jeiHelpers;
        this.type = type;
        IDrawableStatic arrow = jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven.png"), 176, 14, 24, 17);
        this.arrow = jeiHelpers.getGuiHelper().createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.enchanted.kettle");
    }

    @Override
    public void draw(KettleRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        this.arrow.draw(poseStack, 85, 29);
        drawPowerCost(Minecraft.getInstance(), poseStack, "Required Altar Power : " + recipe.getPower(), 0xFFFFFFFF);
    }

    private void drawPowerCost(Minecraft minecraft, PoseStack poseStack, String text, int mainColor) {
        int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
        int width = minecraft.font.width(text);
        int x = GUI_WIDTH/2 - width/2 - 1;
        int y = 55;

        minecraft.font.draw(poseStack, text, x + 1, y, shadowColor);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, KettleRecipe recipe, IFocusGroup focuses) {
        int offset = 0;
        for(ItemStack i : recipe.getItemsIn()){
            builder.addSlot(RecipeIngredientRole.INPUT,5+offset,5).addIngredient(VanillaTypes.ITEM_STACK,i);
            offset += 20;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT,110,30).addIngredient(VanillaTypes.ITEM_STACK,recipe.getResultItem());
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/jei/witch_cauldron.png"), 4, 4, GUI_WIDTH, 70);
    }

    @Override
    public IDrawable getIcon() {
        return jeiHelpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.KETTLE.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return type.getUid();
    }

    @Override
    public Class<? extends KettleRecipe> getRecipeClass() {
        return type.getRecipeClass();
    }
}
