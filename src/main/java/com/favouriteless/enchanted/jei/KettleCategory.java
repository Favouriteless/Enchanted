package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.recipes.KettleRecipe;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
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

public class KettleCategory implements IRecipeCategory<KettleRecipe> {

    IJeiHelpers jeiHelpers;
    RecipeType<KettleRecipe> type;
    IDrawableAnimated arrow;

    public KettleCategory(IJeiHelpers jeiHelpers, RecipeType<KettleRecipe> type) {
        this.jeiHelpers = jeiHelpers;
        this.type = type;
        IDrawableStatic arrow = jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven.png"), 176, 14, 24, 17);
        this.arrow = jeiHelpers.getGuiHelper().createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public Component getTitle() {
        return new TextComponent("Kettle");
    }

    @Override
    public void draw(KettleRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.arrow.draw(stack,75,30);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, KettleRecipe recipe, IFocusGroup focuses) {
        int offset = 0;
        for(ItemStack i : recipe.getItemsIn()){
            builder.addSlot(RecipeIngredientRole.INPUT,10+offset,5).addIngredient(VanillaTypes.ITEM_STACK,i);
            offset += 20;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT,100,30).addIngredient(VanillaTypes.ITEM_STACK,recipe.getResultItem());
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createBlankDrawable(140,60);
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
