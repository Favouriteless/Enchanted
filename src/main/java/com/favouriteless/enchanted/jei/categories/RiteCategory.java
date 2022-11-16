package com.favouriteless.enchanted.jei.categories;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractCreateItemRite;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.jei.EnchantedJEIPlugin;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class RiteCategory implements IRecipeCategory<AbstractCreateItemRite> {

    private final IJeiHelpers jeiHelpers;
    private final RecipeType<AbstractCreateItemRite> recipeTypeRite;

    private static final int GLYPH_SIZE = 110;
    private static final int START_RADIUS = 15;
    private static final int RADIUS_INCREMENT = 15;

    private final IDrawableStatic glyph_golden;
    private final List<IDrawableStatic> circles = new ArrayList<>();
    private final IDrawableAnimated arrow;

    public RiteCategory(IJeiHelpers jeiHelpers, RecipeType<AbstractCreateItemRite> recipeTypeRite) {
        this.jeiHelpers = jeiHelpers;
        this.recipeTypeRite = recipeTypeRite;
        IDrawableStatic arrow = jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/witch_oven.png"), 176, 14, 24, 17);
        this.arrow = jeiHelpers.getGuiHelper().createAnimatedDrawable(arrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
        glyph_golden = buildTexture(Enchanted.location("textures/gui/jei/gold_glyph.png"), GLYPH_SIZE, GLYPH_SIZE, jeiHelpers);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AbstractCreateItemRite rite, IFocusGroup focuses) {
        List<ItemStack> itemList = new ArrayList<>();
        for(Item item : rite.ITEMS_REQUIRED.keySet())
            itemList.add(new ItemStack(item, rite.ITEMS_REQUIRED.get(item)));

        int circleNum = 1;
        int itemsRemaining = itemList.size();

        while(itemsRemaining > 0) {
            int radius = START_RADIUS + (circleNum-1)*RADIUS_INCREMENT;
            int limit = (int)Math.round(Math.pow(6, circleNum));
            int itemCount = Math.min(itemsRemaining, limit);

            for(int i = 0; i < itemCount; i++) {
                ItemStack stack = itemList.get(0);
                itemList.remove(stack);

                double angle = Math.toRadians(i * (360.0D / itemCount) + 180);
                int cx = (int) Math.round(Math.sin(-angle) * radius) - 8;
                int cy = (int) Math.round(Math.cos(-angle) * radius) - 8;

                builder.addSlot(RecipeIngredientRole.INPUT, 47 + cx, 60 + cy).addItemStack(stack);
                itemsRemaining--;
            }
            circleNum++;
        }

        ItemStack[] itemsOut = rite.getResultItems();
        int numRows = (int)Math.ceil(itemsOut.length / 3.0D);
        int height = numRows * 17;
        int startX = 119;
        int startY = 61 - (int)Math.round(height/2.0D);
        for(int i = 0; i < itemsOut.length; i++) {
            ItemStack stack = itemsOut[i];
            builder.addSlot(RecipeIngredientRole.OUTPUT, startX + (i%3)*17, startY + i/3*17).addItemStack(stack);
        }

        circles.clear();
        for(CirclePart circlePart : rite.CIRCLES_REQUIRED.keySet()) {
            Block block = rite.CIRCLES_REQUIRED.get(circlePart);
            ResourceLocation textureLocation = EnchantedJEIPlugin.getCircleTextureLocation(circlePart, block);
            if(textureLocation != null) {
                circles.add(buildTexture(textureLocation, GLYPH_SIZE, GLYPH_SIZE, jeiHelpers));
            }
        }
    }


    @Override
    public void draw(AbstractCreateItemRite rite, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        for(IDrawableStatic drawable : circles) {
            drawable.draw(poseStack, 0, 14);
        }
        glyph_golden.draw(poseStack, 0, 14);
        this.arrow.draw(poseStack, 95, 53);

        ResourceLocation riteName = rite.getType().getRegistryName();
        String nameText = new TranslatableComponent("rite." + riteName.getNamespace() + "." + riteName.getPath()).getString();
        drawText(Minecraft.getInstance(), poseStack, nameText, 180, 0, 0xFFFFFFFF);
        drawText(Minecraft.getInstance(), poseStack, "Required Altar Power : " + rite.POWER, 180, 112, 0xFFFFFFFF);

        if(!rite.ENTITIES_REQUIRED.isEmpty() || rite.hasAdditionalRequirements()) {
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            drawText(Minecraft.getInstance(), poseStack, "Has additional requirements", 360, 18, 0xFFFFFFFF);
            poseStack.popPose();
        }
    }

    private void drawText(Minecraft minecraft, PoseStack poseStack, String text, int x, int y, int mainColor) {
        int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
        int width = minecraft.font.width(text);
        int cx = x/2 - width/2 - 1;
        minecraft.font.draw(poseStack, text, cx + 1, y, shadowColor);
    }

    private IDrawableStatic buildTexture(ResourceLocation resourceLocation, int width, int height, IJeiHelpers helper) {
        IDrawableBuilder builder = helper.getGuiHelper().drawableBuilder(resourceLocation, 0, 0, width, height);
        builder.setTextureSize(width, height);
        return builder.build();
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.enchanted.circle_magic");
    }

    @Override
    public IDrawable getBackground() {
        return jeiHelpers.getGuiHelper().createDrawable(Enchanted.location("textures/gui/jei/circle_magic.png"), 0, 0, 180, 120);
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
