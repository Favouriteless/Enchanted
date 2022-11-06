package com.favouriteless.enchanted.jei;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.recipes.SpinningWheelRecipe;
import com.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
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

public class WitchCauldronCategory implements IRecipeCategory<WitchCauldronRecipe> {

    IJeiHelpers jeiHelpers;
    RecipeType<WitchCauldronRecipe> type;

    IDrawableAnimated arrow;



    public WitchCauldronCategory(IJeiHelpers jeiHelpers, RecipeType<WitchCauldronRecipe> type) {
        this.jeiHelpers = jeiHelpers;
        this.type = type;

        IDrawableStatic arrow = jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven.png"), 176, 14, 24, 17);
        this.arrow = jeiHelpers.getGuiHelper().createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void draw(WitchCauldronRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.arrow.draw(stack,75,30);
    }

    @Override
    public Component getTitle() {
        return new TextComponent("Witch Cauldron");
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createBlankDrawable(140,60);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, WitchCauldronRecipe recipe, IFocusGroup focuses) {
        int offset = 0;
        for(ItemStack i : recipe.getItemsIn()){
            builder.addSlot(RecipeIngredientRole.INPUT,10+offset,5).addIngredient(VanillaTypes.ITEM_STACK,i);
            offset += 20;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT,100,30).addIngredient(VanillaTypes.ITEM_STACK,recipe.getResultItem());
    }

    @Override
    public IDrawable getIcon() {
        return jeiHelpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.WITCH_CAULDRON.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return type.getUid();
    }

    @Override
    public Class<? extends WitchCauldronRecipe> getRecipeClass() {
        return type.getRecipeClass();
    }
}
