package net.favouriteless.enchanted.client.screens;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.menus.DistilleryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DistilleryScreen extends AbstractContainerScreen<DistilleryMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.id("textures/gui/menus/distillery.png");

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
    public void render(@NotNull GuiGraphics gui, int xMouse, int yMouse, float partialTicks) {
        renderBackground(gui, xMouse, yMouse, partialTicks);
        super.render(gui, xMouse, yMouse, partialTicks);
        this.renderTooltip(gui, xMouse, yMouse);
    }


    @Override
    protected void renderBg(@NotNull GuiGraphics gui, float partialTicks, int x, int y) {
        int edgeSpacingX = (width - imageWidth) / 2;
        int edgeSpacingY = (height - imageHeight) / 2;
        gui.blit(TEXTURE, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight);

        // Draw cook progress bar
        int cookProgress = menu.getCookProgress();
        int cookDuration = menu.getCookDuration();
        int cookProgressionScaled = cookDuration != 0 && cookProgress != 0 ? cookProgress * COOK_BAR_WIDTH / cookDuration : 0;

        gui.blit(TEXTURE, leftPos + COOK_BAR_XPOS, topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);

        int bubbleOffset = BUBBLELENGTHS[cookProgress / 2 % 7];
        if (bubbleOffset > 0)
            gui.blit(TEXTURE, leftPos + BUBBLES_XPOS, topPos + BUBBLES_YPOS - bubbleOffset, BUBBLES_ICON_U, BUBBLES_ICON_V - bubbleOffset, 12, bubbleOffset);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics gui, int xMouse, int yMouse) {
        gui.drawString(font, title, (imageWidth / 2 - font.width(title) / 2), titleLabelY, Color.DARK_GRAY.getRGB(), false);
        gui.drawString(font, minecraft.player.getInventory().getDisplayName(), inventoryLabelX, inventoryLabelY, Color.DARK_GRAY.getRGB(), false);
    }

}