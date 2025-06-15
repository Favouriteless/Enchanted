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
import net.favouriteless.enchanted.common.circle_magic.CircleMagicShape;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.integrations.jei.EJeiRecipeTypes;
import net.favouriteless.enchanted.integrations.jei.recipes.JeiRiteRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class RiteCategory extends AbstractRecipeCategory<JeiRiteRecipe> {

    private static final int CIRCLE_SIZE = 110;
    private static final int START_RADIUS = 15;
    private static final int RADIUS_INCREMENT = 15;

    private final IGuiHelper helper;
    private final IDrawableStatic background;
    private final IDrawableStatic glyph_golden;
    private final IDrawableAnimated arrow;

    private final List<IDrawableStatic> circles = new ArrayList<>();

    public RiteCategory(IGuiHelper helper) {
        super(
                EJeiRecipeTypes.RITE,
                Component.translatable("jei.enchanted.circle_magic"),
                helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(EItems.RITUAL_CHALK.get())),
                180, 120
        );
        this.helper = helper;

        background = helper.createDrawable(Enchanted.id("textures/gui/jei/circle_magic.png"), 0, 0, 180, 120);
        glyph_golden = buildTexture(Enchanted.id("textures/gui/jei/gold_glyph.png"), CIRCLE_SIZE, CIRCLE_SIZE);
        arrow = helper.createAnimatedRecipeArrow(120);
    }

    @Override
    public void draw(JeiRiteRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        for(IDrawableStatic drawable : circles) {
            drawable.draw(graphics, 0, 14);
        }
        glyph_golden.draw(graphics, 0, 14);
        arrow.draw(graphics, 95, 53);

        ResourceLocation riteName = recipe.id();
        String nameText = Component.translatable("rite." + riteName.getNamespace() + "." + riteName.getPath()).getString();
        drawText(graphics, nameText, 180, 0);
        drawText(graphics, "Required Altar Power : " + recipe.rite().getPower(), 180, 112);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiRiteRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> itemsOut = recipe.rite().getOutputs();
        if(itemsOut == null)
            return;

        List<ItemStack> itemList = new ArrayList<>(recipe.rite().getItems());

        int circleNum = 1;
        int itemsRemaining = itemList.size();

        while(itemsRemaining > 0) {
            int radius = START_RADIUS + (circleNum - 1) * RADIUS_INCREMENT;
            int limit = (int)Math.round(Math.pow(6, circleNum));
            int itemCount = Math.min(itemsRemaining, limit);

            for(int i = 0; i < itemCount; i++) {
                ItemStack stack = itemList.getFirst();
                itemList.remove(stack);

                float angle = i * Mth.TWO_PI / itemCount + Mth.PI;
                int cx = Math.round(Mth.sin(-angle) * radius) - 8;
                int cy = Math.round(Mth.cos(-angle) * radius) - 8;

                builder.addSlot(RecipeIngredientRole.INPUT, 47 + cx, 60 + cy).addItemStack(stack);
                itemsRemaining--;
            }
            circleNum++;
        }


        int numRows = (int)Math.ceil(itemsOut.size() / 3.0F);
        int height = numRows * 17;
        int startX = 119;
        int startY = 61 - Math.round(height / 2.0F);
        for(int i = 0; i < itemsOut.size(); i++) {
            ItemStack stack = itemsOut.get(i);
            builder.addSlot(RecipeIngredientRole.OUTPUT, startX + (i % 3) * 17, startY + i / 3 * 17).addItemStack(stack);
        }

        circles.clear();
        for(Entry<Holder<CircleMagicShape>, Block> entry : recipe.rite().getShapes().entrySet()) {
            ResourceKey<CircleMagicShape> shapeKey = entry.getKey().unwrapKey().orElse(null);
            if(shapeKey == null)
                return;

            ResourceLocation location = shapeKey.location();
            ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(
                    location.getNamespace(),
                    String.format("textures/gui/circle_magic_shapes/%s_%s.png", location.getPath(), BuiltInRegistries.BLOCK.getKey(entry.getValue()).getPath())
            );

            if(Minecraft.getInstance().getResourceManager().getResource(texture).isPresent()) {
                circles.add(buildTexture(texture, CIRCLE_SIZE, CIRCLE_SIZE));
            }
        }
    }

    private void drawText(GuiGraphics graphics, String text, int x, int y) {
        Minecraft mc = Minecraft.getInstance();
        int width = mc.font.width(text);
        int cx = x / 2 - width / 2 - 1;
        graphics.drawString(mc.font, text, cx + 1, y, Color.DARK_GRAY.getRGB(), false);
    }

    private IDrawableStatic buildTexture(ResourceLocation id, int width, int height) {
        return helper.drawableBuilder(id, 0, 0, width, height).setTextureSize(width, height).build();
    }

}
