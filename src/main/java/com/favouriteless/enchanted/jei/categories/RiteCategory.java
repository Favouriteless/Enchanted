package com.favouriteless.enchanted.jei.categories;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class RiteCategory implements IRecipeCategory<AbstractCreateItemRite> {

    IJeiHelpers jeiHelpers;
    RecipeType<AbstractCreateItemRite> recipeTypeRite;

    private static final int MAXSIZE = 15;
    private static final int GLYPH_SIZE = 8;
    private final IDrawableStatic glyph_white;
    private final IDrawableStatic glyph_golden;


    public RiteCategory(IJeiHelpers jeiHelpers, RecipeType<AbstractCreateItemRite> recipeTypeRite) {
        this.jeiHelpers = jeiHelpers;
        this.recipeTypeRite = recipeTypeRite;


        IDrawableBuilder glyphDrawableBuilder = jeiHelpers.getGuiHelper().drawableBuilder(Enchanted.location("textures/block/glyph_0.png"), 0, 0, GLYPH_SIZE, GLYPH_SIZE);
        glyphDrawableBuilder.setTextureSize(GLYPH_SIZE, GLYPH_SIZE);
        this.glyph_white = glyphDrawableBuilder.build();
        glyphDrawableBuilder = jeiHelpers.getGuiHelper().drawableBuilder(Enchanted.location("textures/block/glyph_gold.png"), 0, 0, GLYPH_SIZE, GLYPH_SIZE);
        glyphDrawableBuilder.setTextureSize(GLYPH_SIZE, GLYPH_SIZE);
        this.glyph_golden = glyphDrawableBuilder.build();

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractCreateItemRite recipe, IFocusGroup focuses) {
        int i = 120;
        int j = 0;
        for (Map.Entry<Item, Integer> entry : recipe.ITEMS_REQUIRED.entrySet()) {
            if (i + 20 > 180) {
                i = 120;
                j += 20;
            }
            builder.addSlot(RecipeIngredientRole.INPUT, i, j).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(entry.getKey(), entry.getValue()));
            i += 20;
        }
        i = 120;
        for (ItemStack item : recipe.getResultItems()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, i, 120 - (j + 20)).addIngredient(VanillaTypes.ITEM_STACK, item);
            i += 20;
        }
    }

    @Override
    public void draw(AbstractCreateItemRite recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        for (CirclePart circlePart : recipe.CIRCLES_REQUIRED.keySet()) {
            for (int x = 0; x < MAXSIZE; x++) {
                for (int y = 0; y < MAXSIZE; y++) {
                    if (circlePart.getCirclePoints().contains(new BlockPos(x - MAXSIZE / 2, 0, y - MAXSIZE / 2))) {
                        glyph_white.draw(stack, x * GLYPH_SIZE, y * GLYPH_SIZE);
                    } else if (x - MAXSIZE / 2 == 0 && y - MAXSIZE / 2 == 0) {
                        glyph_golden.draw(stack, x * GLYPH_SIZE, y * GLYPH_SIZE);
                    }
                }
            }
        }


    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.enchanted.circle_magic");
    }

    @Override
    public IDrawable getBackground() {
        return this.jeiHelpers.getGuiHelper().createBlankDrawable(180, 120);
    }

    @Override
    public IDrawable getIcon() {
        return this.jeiHelpers.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EnchantedItems.CHALK_WHITE.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return this.recipeTypeRite.getUid();
    }

    @Override
    public Class<? extends AbstractCreateItemRite> getRecipeClass() {
        return this.recipeTypeRite.getRecipeClass();
    }
}
