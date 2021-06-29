package com.favouriteless.enchanted.client.screens;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.containers.DistilleryContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

public class DistilleryScreen extends ContainerScreen<DistilleryContainer> {

    private DistilleryContainer container;

    // This is the resource location for the background image
    private static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/gui/distillery.png");

    // some [x,y] coordinates of graphical elements
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
    public static final int BUBBLES_WIDTH = 12;
    public static final int BUBBLES_HEIGHT = 29;

    private static final int[] BUBBLELENGTHS = new int[]{0, 6, 11, 16, 20, 24, 29};

    public DistilleryScreen(DistilleryContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;

        // Set the width and height of the gui.  Should match the size of the texture!
        imageWidth = 176;
        imageHeight = 166;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    // Draw the Tool tip text if hovering over something of interest on the screen
    protected void renderHoveredTooltip(MatrixStack matrixStack, int x, int y) {
        if (!this.minecraft.player.inventory.getCarried().isEmpty()) return;  // no tooltip if the player is dragging something
            super.renderTooltip(matrixStack, x, y);
    }


    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);

        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);

        // Draw cook progress bar
        int cookProgressionScaled = this.container.getCookProgressionScaled(COOK_BAR_WIDTH);
        this.blit(matrixStack, this.leftPos + COOK_BAR_XPOS, this.topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);

        int cookProgression = this.container.getCookProgression();
        int bubbleOffset = BUBBLELENGTHS[cookProgression / 2 % 7];
        if (bubbleOffset > 0) {
            this.blit(matrixStack, this.leftPos + BUBBLES_XPOS, this.topPos + BUBBLES_YPOS - bubbleOffset, BUBBLES_ICON_U, BUBBLES_ICON_V - bubbleOffset, 12, bubbleOffset);
        }

    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        final int LABEL_XPOS = 62;
        final int LABEL_YPOS = 5;
        font.draw(matrixStack, title.getContents(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
    }

    // Returns true if the given x,y coordinates are within the given rectangle
    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
        return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
    }
}