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
import net.favouriteless.enchanted.common.recipes.DistillingRecipe;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DistillingCategory extends AbstractRecipeCategory<DistillingRecipe> {

    private final IDrawableStatic background;
    private final IDrawableAnimated bubbles;
    private final IDrawableAnimated arrow;

    public DistillingCategory(IGuiHelper helper) {
        super(
                EJeiRecipeTypes.DISTILLING,
                Component.translatable("container.enchanted.distillery"),
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EItems.DISTILLERY.get())),
                146, 75
        );


        background = helper.createDrawable(Enchanted.id("textures/gui/distillery.png"), 4, 5, getWidth(), getHeight());

        bubbles = helper.createAnimatedDrawable(
                helper.createDrawable(Enchanted.id("textures/gui/distillery.png"), 176, 0, 12, 29),
                120, IDrawableAnimated.StartDirection.BOTTOM, false
        );
        arrow = helper.createAnimatedDrawable(
                helper.createDrawable(Enchanted.id("textures/gui/distillery.png"), 176, 29, 57, 61),
                120, IDrawableAnimated.StartDirection.LEFT, false
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DistillingRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> inputs = recipe.getInputs();

        boolean hasJars = false;
        for(ItemStack stack : inputs) {
            if(stack.is(EItems.CLAY_JAR.get())) {
                hasJars = true;
                break;
            }
        }

        int offset;
        if(hasJars)
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 30).addIngredient(VanillaTypes.ITEM_STACK, inputs.getFirst());

        offset = 0;
        for(ItemStack i : recipe.getInputs()) {
            if(!i.is(EItems.CLAY_JAR.get())) {
                builder.addSlot(RecipeIngredientRole.INPUT, 50, 20 + offset).addIngredient(VanillaTypes.ITEM_STACK, i);
                offset += 20;
            }
        }

        offset = 0;
        for(ItemStack i : recipe.getOutputs()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 123, 2 + offset).addIngredient(VanillaTypes.ITEM_STACK, i);
            offset += 19;
        }
    }

    @Override
    public void draw(DistillingRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        bubbles.draw(graphics, 88, 22);
        arrow.draw(graphics, 65, 8);
    }

}