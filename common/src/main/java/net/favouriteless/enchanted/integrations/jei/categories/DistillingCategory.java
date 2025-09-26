package net.favouriteless.enchanted.integrations.jei.categories;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.registry.EItems;
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class DistillingCategory implements IRecipeCategory<DistillingRecipe> {

    private final IGuiHelper helper;

    private final IDrawableAnimated bubbles;
    private final IDrawableAnimated arrow;


    public DistillingCategory(IGuiHelper helper) {
        this.helper = helper;
        IDrawableStatic bubbles = helper.createDrawable(Enchanted.id("textures/gui/distillery.png"), 176, 0, 12, 29);
        this.bubbles = helper.createAnimatedDrawable(bubbles, 120, IDrawableAnimated.StartDirection.BOTTOM, false);
        IDrawableStatic arrow = helper.createDrawable(Enchanted.id("textures/gui/distillery.png"), 176, 29, 57, 61);
        this.arrow = helper.createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void draw(DistillingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics gui, double mouseX, double mouseY) {
        this.bubbles.draw(gui, 88, 22);
        this.arrow.draw(gui, 65, 8);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DistillingRecipe recipe, IFocusGroup focuses) {
        int offset = 20;
        for (ItemStack i : recipe.getItemsIn()) {
            // Special case "container" items. Will break if multiple stacks are present.
            // Should probably be a tag check.
            if (i.is(EItems.CLAY_JAR.get())) {
                builder.addSlot(RecipeIngredientRole.INPUT, 28, 30).addIngredient(VanillaTypes.ITEM_STACK, i);
                continue;
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 50, offset).addIngredient(VanillaTypes.ITEM_STACK, i);
            offset += 20;
        }
        offset = 0;
        for (ItemStack i : recipe.getItemsOut()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 2 + offset).addIngredient(VanillaTypes.ITEM_STACK, i);
            offset += 19;
        }
    }

    @Override
    public Component getTitle() {
        return Component.translatable("container.enchanted.distillery");
    }

    @Override
    public IDrawable getBackground() {
        return this.helper.createDrawable(Enchanted.id("textures/gui/distillery.png"), 4, 5, 146, 75);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EItems.DISTILLERY.get()));
    }

    @Override
    public RecipeType<DistillingRecipe> getRecipeType() {
        return EJeiRecipeTypes.DISTILLING;
    }
}