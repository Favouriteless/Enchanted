package net.favouriteless.enchanted.integrations.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.recipes.SpinningRecipe;
import net.favouriteless.enchanted.common.util.RecipeUtils;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.List;

public class SpinningCategory extends AbstractRecipeCategory<SpinningRecipe> {

    private static final int[][] SLOT_POSITIONS = new int[][] { { 25, 13 }, { 13, 37 }, { 37, 37 } };

    private final IDrawableStatic background;
    private final IDrawableAnimated leftArrow;
    private final IDrawableAnimated rightArrow;

    public SpinningCategory(IGuiHelper helper) {
        super(
                EJeiRecipeTypes.SPINNING,
                Component.translatable("container.enchanted.spinning_wheel"),
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EItems.SPINNING_WHEEL.get())),
                140, 75
        );


        background = helper.createDrawable(Enchanted.id("textures/gui/menus/spinning_wheel.png"), 20, 10, getWidth(), 60);

        leftArrow = helper.createAnimatedDrawable(
                helper.createDrawable(Enchanted.id("textures/gui/menus/spinning_wheel.png"), 176, 0, 15, 20),
                120, IDrawableAnimated.StartDirection.BOTTOM, false
        );
        rightArrow = helper.createAnimatedDrawable(
                helper.createDrawable(Enchanted.id("textures/gui/menus/spinning_wheel.png"), 176, 20, 15, 20),
                120, IDrawableAnimated.StartDirection.BOTTOM, false
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SpinningRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> inputs = recipe.getInputs();
        for (int i = 0; i < inputs.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, SLOT_POSITIONS[i][0], SLOT_POSITIONS[i][1])
                   .addIngredient(VanillaTypes.ITEM_STACK, inputs.get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 110, 25).addIngredient(VanillaTypes.ITEM_STACK, RecipeUtils.getResultItem(recipe));
    }

    @Override
    public void draw(SpinningRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        leftArrow.draw(graphics, 7, 16);
        rightArrow.draw(graphics, 44, 16);
        drawPowerCost(graphics, recipe);
    }

    private void drawPowerCost(GuiGraphics graphics, SpinningRecipe recipe) {
        Minecraft mc = Minecraft.getInstance();
        String text = "Required Altar Power : " + recipe.getPower();
        graphics.drawString(mc.font, text, getWidth() / 2 - mc.font.width(text) / 2, 65, Color.DARK_GRAY.getRGB(), false);
    }

}