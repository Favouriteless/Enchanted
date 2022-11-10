package com.favouriteless.enchanted.jei.categories;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.api.rites.AbstractRite;
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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class RiteCategory implements IRecipeCategory<AbstractRite> {

    IJeiHelpers jeiHelpers;
    RecipeType<AbstractRite> recipeTypeRite;

    private static final int GLYPH_SIZE = 100;

    private final IDrawableStatic glyph_golden;
    private final IDrawableStatic glyph_white[] = new IDrawableStatic[3];

    private final IDrawableStatic[] glyph_red = new IDrawableStatic[3];

    private final IDrawableStatic[] glyph_purple = new IDrawableStatic[3];

    private IDrawableStatic buildTexture(ResourceLocation resourceLocation, int width, int height, IJeiHelpers helper) {
        IDrawableBuilder builder = helper.getGuiHelper().drawableBuilder(resourceLocation, 0, 0, width, height);
        builder.setTextureSize(width, height);
        return builder.build();
    }

    public RiteCategory(IJeiHelpers jeiHelpers, RecipeType<AbstractRite> recipeTypeRite) {
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
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractRite recipe, IFocusGroup focuses) {
        int i = 100;
        int j = 20;
        for (Map.Entry<Item, Integer> entry : recipe.ITEMS_REQUIRED.entrySet()) {
            if (i + 20 > 180) {
                i = 100;
                j += 20;
            }
            builder.addSlot(RecipeIngredientRole.INPUT, i, j).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(entry.getKey(), entry.getValue()));
            i += 20;
        }
        if(recipe instanceof AbstractCreateItemRite){
            AbstractCreateItemRite recipe2 = (AbstractCreateItemRite) recipe;
            for (ItemStack item : recipe2.getResultItems()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 130 , 80).addIngredient(VanillaTypes.ITEM_STACK, item);
            }
        }

    }


    @Override
    public void draw(AbstractRite recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        String circlesRequiredText = "";
        for (CirclePart circlePart : recipe.CIRCLES_REQUIRED.keySet()) {
            IDrawableStatic[] texture = recipe.CIRCLES_REQUIRED.get(circlePart).equals(EnchantedBlocks.CHALK_WHITE.get()) ? glyph_white : (recipe.CIRCLES_REQUIRED.get(circlePart).equals(EnchantedBlocks.CHALK_RED.get()) ? glyph_red : glyph_purple);
            switch (circlePart){
                case LARGE -> {
                    if(circlesRequiredText!="")circlesRequiredText+="-";
                    circlesRequiredText+="15x15";
                    texture[2].draw(stack,0,20);
                }
                case MEDIUM -> {
                    if(circlesRequiredText!="")circlesRequiredText+="-";
                    circlesRequiredText+="11x11";
                    texture[1].draw(stack,0,20);
                }
                default -> {
                    if(circlesRequiredText!="")circlesRequiredText+="-";
                    circlesRequiredText+="7x7";
                    texture[0].draw(stack,0,20);
                }
            }
        }
        glyph_golden.draw(stack,0,20);

        int i = 60;
        for(EntityType e : recipe.ENTITIES_REQUIRED.keySet()){
            System.out.println(e.getCategory().getName());
            drawText(Minecraft.getInstance(),stack,e.getCategory().getName(),100,i,0xFFFFFFFF);
            i+=20;
        }

        drawText(Minecraft.getInstance(),stack,circlesRequiredText,100,107,0xFFFFFFFF);
        drawText(Minecraft.getInstance(),stack,recipe.getType().getRegistryName().toString(),180,0,0xFFFFFFFF);
    }
    private void drawText(Minecraft minecraft, PoseStack poseStack, String text,int x,int y, int mainColor) {
        int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
        int width = minecraft.font.width(text);
        int X = x/2 - width/2 - 1;
        int Y = y;

        minecraft.font.draw(poseStack, text, X + 1, Y, shadowColor);
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
    public Class<? extends AbstractRite> getRecipeClass() {
        return this.recipeTypeRite.getRecipeClass();
    }
}
