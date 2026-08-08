package net.favouriteless.enchanted.integrations.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.favouriteless.enchanted.common.util.RecipeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class KettleRecipeCategory extends AbstractRecipeCategory<KettleRecipe> {

    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;

    public KettleRecipeCategory(IGuiHelper guiHelper, RecipeType<KettleRecipe> type, Component title, Item icon) {
        super(
                type,
                title,
                guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(icon)),
                140, 70
        );

        background = guiHelper.createDrawable(Enchanted.id("textures/gui/jei/witch_cauldron.png"), 4, 4, 140, 70);
        arrow = guiHelper.createAnimatedRecipeArrow(120);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, KettleRecipe recipe, IFocusGroup focuses) {
        int offset = 0;
        for (ItemStack i : recipe.getInputs()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 5 + offset, 5).addIngredient(VanillaTypes.ITEM_STACK, i);
            offset += 20;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 30).addIngredient(VanillaTypes.ITEM_STACK, RecipeUtils.getResultItem(recipe));
    }

    @Override
    public void draw(KettleRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        arrow.draw(graphics, 85, 29);
        drawPowerCost(graphics, recipe);
    }

    private void drawPowerCost(GuiGraphics graphics, KettleRecipe recipe) {
        Minecraft mc = Minecraft.getInstance();
        String text = "Required Altar Power : " + recipe.getPower();
        graphics.drawString(mc.font, text, 70 - mc.font.width(text) / 2, 55, Color.DARK_GRAY.getRGB(), false);
    }

}