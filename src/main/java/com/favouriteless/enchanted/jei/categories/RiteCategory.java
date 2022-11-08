package com.favouriteless.enchanted.jei.categories;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class RiteCategory implements IRecipeCategory<AbstractCreateItemRite> {

    IJeiHelpers jeiHelpers;
    RecipeType<AbstractCreateItemRite> recipeTypeRite;

    private static final int GLYPH_SIZE = 128;

    private final IDrawableStatic glyph_golden;
    private final IDrawableStatic glyph_white[] = new IDrawableStatic[3];

    private final IDrawableStatic[] glyph_red = new IDrawableStatic[3];

    private final IDrawableStatic[] glyph_purple = new IDrawableStatic[3];

    private IDrawableStatic buildTexture(ResourceLocation resourceLocation, int width, int height, IJeiHelpers helper) {
        IDrawableBuilder builder = helper.getGuiHelper().drawableBuilder(resourceLocation, 0, 0, width, height);
        builder.setTextureSize(width, height);
        return builder.build();
    }

    public RiteCategory(IJeiHelpers jeiHelpers, RecipeType<AbstractCreateItemRite> recipeTypeRite) {
        this.jeiHelpers = jeiHelpers;
        this.recipeTypeRite = recipeTypeRite;

        glyph_white[0] = buildTexture(Enchanted.location("textures/patchouli/white_small.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
        glyph_white[1] = buildTexture(Enchanted.location("textures/patchouli/white_medium.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
        glyph_white[2] = buildTexture(Enchanted.location("textures/patchouli/white_large.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);

        glyph_red[0] = buildTexture(Enchanted.location("textures/patchouli/red_small.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
        glyph_red[1] = buildTexture(Enchanted.location("textures/patchouli/red_medium.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
        glyph_red[2] = buildTexture(Enchanted.location("textures/patchouli/red_large.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);

        glyph_purple[0] = buildTexture(Enchanted.location("textures/patchouli/purple_small.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
        glyph_purple[1] = buildTexture(Enchanted.location("textures/patchouli/purple_medium.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
        glyph_purple[2] = buildTexture(Enchanted.location("textures/patchouli/purple_large.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);

        glyph_golden = buildTexture(Enchanted.location("textures/patchouli/gold.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);


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

    private IDrawableStatic getGlyphText(CirclePart size, Block type) {
        IDrawableStatic[] texture = type.equals(EnchantedBlocks.CHALK_WHITE.get()) ? glyph_white : (type.equals(EnchantedBlocks.CHALK_RED.get()) ? glyph_red : glyph_purple);
        switch (size) {
            case MEDIUM -> {
                return texture[1];
            }
            case LARGE -> {
                return texture[2];
            }
            default -> {
                return texture[0];
            }
        }
    }

    @Override
    public void draw(AbstractCreateItemRite recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        for (CirclePart circlePart : recipe.CIRCLES_REQUIRED.keySet()) {
            getGlyphText(circlePart, recipe.CIRCLES_REQUIRED.get(circlePart)).draw(stack, 0, 0);
        }
        glyph_golden.draw(stack);

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
