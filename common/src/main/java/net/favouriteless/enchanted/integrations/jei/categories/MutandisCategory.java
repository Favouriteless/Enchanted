package net.favouriteless.enchanted.integrations.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.integrations.jei.recipes.JeiMutandisRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class MutandisCategory extends AbstractRecipeCategory<JeiMutandisRecipe> {

    private final IDrawableStatic background;

    public MutandisCategory(IGuiHelper helper, RecipeType<JeiMutandisRecipe> type, Item icon, Component title) {
        super(type, title, helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(icon)), 120, 68);
        background = helper.createDrawable(Enchanted.id("textures/gui/jei/mutandis.png"), 0, 0, getWidth(), getHeight());
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiMutandisRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 35).addItemStacks(recipe.getInputs());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 35).addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(JeiMutandisRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        drawText(graphics, recipe.getDescription(), 120, 10, Color.DARK_GRAY.getRGB());
    }

    private void drawText(GuiGraphics graphics, String text, int x, int y, int colour) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(text);
        if (width > 150) {
            drawText(graphics, text.substring(0, text.length() / 2), x, y, colour);
            drawText(graphics, text.substring(text.length() / 2), x, y + 10, colour);
            return;
        }
        int cx = x / 2 - width / 2 - 1;
        graphics.drawString(mc.font, text, cx + 1, y, colour, false);
    }

}