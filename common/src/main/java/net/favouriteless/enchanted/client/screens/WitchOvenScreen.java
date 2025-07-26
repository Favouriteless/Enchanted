package net.favouriteless.enchanted.client.screens;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.menus.WitchOvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class WitchOvenScreen extends AbstractContainerScreen<WitchOvenMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.id("textures/gui/menus/witch_oven.png");

    public static final int COOK_BAR_XPOS = 76;
    public static final int COOK_BAR_YPOS = 16;
    public static final int COOK_BAR_ICON_U = 176;
    public static final int COOK_BAR_ICON_V = 14;
    public static final int COOK_BAR_WIDTH = 24;
    public static final int COOK_BAR_HEIGHT = 17;

    public static final int FLAME_XPOS = 80;
    public static final int FLAME_YPOS = 36;
    public static final int FLAME_ICON_U = 176;
    public static final int FLAME_ICON_V = 12;
    public static final int FLAME_SIZE = 14;

    public WitchOvenScreen(WitchOvenMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void render(GuiGraphics gui, int xMouse, int yMouse, float partialTicks) {
        renderBackground(gui, xMouse, yMouse, partialTicks);
        super.render(gui, xMouse, yMouse, partialTicks);
        renderTooltip(gui, xMouse, yMouse);
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTicks, int x, int y) {
        int edgeSpacingX = (width - imageWidth) / 2;
        int edgeSpacingY = (height - imageHeight) / 2;
        gui.blit(TEXTURE, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight);

        int cookProgressionScaled = getCookProgressionScaled();
        gui.blit(TEXTURE, leftPos + COOK_BAR_XPOS, topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);


        // Draw fuel remaining bar
        if (menu.getBurnDuration() > 0) {
            int burnLeftScaled = getBurnLeftScaled();
            gui.blit(TEXTURE, leftPos + FLAME_XPOS, topPos + FLAME_YPOS + 12 - burnLeftScaled, FLAME_ICON_U, FLAME_ICON_V - burnLeftScaled, FLAME_SIZE, burnLeftScaled + 1);
        }

    }

    @Override
    protected void renderLabels(GuiGraphics gui, int xMouse, int yMouse) {
        gui.drawString(font, title, (imageWidth / 2 - font.width(title) / 2), titleLabelY, Color.DARK_GRAY.getRGB(), false);
        gui.drawString(font, minecraft.player.getInventory().getDisplayName(), inventoryLabelX, inventoryLabelY, Color.DARK_GRAY.getRGB(), false);
    }

    public int getBurnLeftScaled() {
        int burnDuration = menu.getBurnDuration();
        int burnProgress = menu.getBurnProgress();

        if (burnDuration == 0)
            burnDuration = 200;

        return burnProgress * 13 / burnDuration;
    }

    public int getCookProgressionScaled() {
        int cookProgress = menu.getCookProgress();
        int cookDuration = menu.getCookDuration();

        return cookDuration != 0 && cookProgress != 0 ? cookProgress * COOK_BAR_WIDTH / cookDuration : 0;
    }

}