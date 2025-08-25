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
import net.favouriteless.enchanted.common.recipes.ByproductRecipe;
import net.favouriteless.enchanted.common.util.RecipeUtils;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;

import java.util.ArrayList;
import java.util.List;

public class ByproductCategory extends AbstractRecipeCategory<ByproductRecipe> {

    private final IDrawableStatic background;
    private final IDrawableAnimated fire;
    private final IDrawableAnimated arrow;

    public ByproductCategory(IGuiHelper helper) {
        super(
                EJeiRecipeTypes.BYPRODUCT,
                Component.translatable("container.enchanted.witch_oven"),
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EItems.WITCH_OVEN.get())),
                96, 65
        );


        background = helper.createDrawable(Enchanted.id("textures/gui/menus/witch_oven.png"), 40, 10, getWidth(), getHeight());
        fire = helper.createAnimatedRecipeFlame(120);
        arrow = helper.createAnimatedRecipeArrow(120);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ByproductRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> itemsOut = new ArrayList<>();
        for(ItemStack stack : recipe.getInput().getItems()) {
            Minecraft mc = Minecraft.getInstance();
            mc.level.getRecipeManager().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.SMELTING, new SingleRecipeInput(stack), mc.level)
                    .ifPresent(holder -> itemsOut.add(RecipeUtils.getResultItem(holder)));
        }

        if(!itemsOut.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 13, 7).addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 67, 7).addItemStacks(itemsOut);
            builder.addSlot(RecipeIngredientRole.OUTPUT, 67, 43).addIngredient(VanillaTypes.ITEM_STACK, RecipeUtils.getResultItem(recipe));
            builder.addSlot(RecipeIngredientRole.CATALYST, 13, 43).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EItems.CLAY_JAR.get(), RecipeUtils.getResultItem(recipe).getCount()));
        }
    }

    @Override
    public void draw(ByproductRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        fire.draw(graphics, 40, 27);
        arrow.draw(graphics, 36, 6);
    }

}