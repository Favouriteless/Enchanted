package com.favouriteless.enchanted.client.screens;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.menus.DistilleryMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class DistilleryScreen extends AbstractContainerScreen<DistilleryMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.location("textures/gui/distillery.png");

    public static final int COOK_BAR_XPOS = 69;
    public static final int COOK_BAR_YPOS = 12;
    public static final int COOK_BAR_ICON_U = 176;
    public static final int COOK_BAR_ICON_V = 29;
    public static final int COOK_BAR_WIDTH = 56;
    public static final int COOK_BAR_HEIGHT = 62;

    public static final int BUBBLES_XPOS = 93;
    public static final int BUBBLES_YPOS = 56;
    public static final int BUBBLES_ICON_U = 176;
    public static final int BUBBLES_ICON_V = 28;

    private static final int[] BUBBLELENGTHS = new int[]{0, 6, 11, 16, 20, 24, 29};

    public DistilleryScreen(DistilleryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    public void render(PoseStack poseStack, int xMouse, int yMouse, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, xMouse, yMouse, partialTicks);
        this.renderTooltip(poseStack, xMouse, yMouse);
    }


    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);;

        int edgeSpacingX = (width - imageWidth) / 2;
        int edgeSpacingY = (height - imageHeight) / 2;
        blit(poseStack, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight);

        // Draw cook progress bar
        int cookTime = menu.getData().get(0);
        int cookTimeTotal = menu.getData().get(1);
        int cookProgressionScaled = cookTimeTotal != 0 && cookTime != 0 ? cookTime * COOK_BAR_WIDTH / cookTimeTotal : 0;


        blit(poseStack, leftPos + COOK_BAR_XPOS, topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);

        int bubbleOffset = BUBBLELENGTHS[cookTime / 2 % 7];
        if (bubbleOffset > 0)
            blit(poseStack, leftPos + BUBBLES_XPOS, topPos + BUBBLES_YPOS - bubbleOffset, BUBBLES_ICON_U, BUBBLES_ICON_V - bubbleOffset, 12, bubbleOffset);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int xMouse, int yMouse) {
        font.draw(poseStack, title, (float)(imageWidth / 2 - font.width(title) / 2), (float)titleLabelY, Color.DARK_GRAY.getRGB());
        font.draw(poseStack, minecraft.player.getInventory().getDisplayName(), (float)inventoryLabelX, (float)inventoryLabelY, Color.DARK_GRAY.getRGB());
    }

}