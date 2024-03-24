package com.favouriteless.enchanted.client.screens;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.blocks.entity.WitchOvenBlockEntity;
import com.favouriteless.enchanted.common.menus.WitchOvenMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class WitchOvenScreen extends AbstractContainerScreen<WitchOvenMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.location("textures/gui/witch_oven.png");

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

    public void render(PoseStack poseStack, int xMouse, int yMouse, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, xMouse, yMouse, partialTicks);
        renderTooltip(poseStack, xMouse, yMouse);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int edgeSpacingX = (width - imageWidth) / 2;
        int edgeSpacingY = (height - imageHeight) / 2;
        blit(poseStack, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight);

        int cookProgressionScaled = getCookProgressionScaled();
        blit(poseStack, leftPos + COOK_BAR_XPOS, topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);


        // Draw fuel remaining bar
        if (menu.getBlockEntity().getBurnDuration() > 0) {
            int burnLeftScaled = getBurnLeftScaled();
            blit(poseStack, leftPos + FLAME_XPOS, topPos + FLAME_YPOS + 12 - burnLeftScaled, FLAME_ICON_U, FLAME_ICON_V - burnLeftScaled, FLAME_SIZE, burnLeftScaled + 1);
        }

    }

    @Override
    protected void renderLabels(PoseStack poseStack, int xMouse, int yMouse) {
        font.draw(poseStack, title, (float)(imageWidth / 2 - font.width(title) / 2), titleLabelY, Color.DARK_GRAY.getRGB());
        font.draw(poseStack, minecraft.player.getInventory().getDisplayName(), inventoryLabelX, inventoryLabelY, Color.DARK_GRAY.getRGB());
    }

    public int getBurnLeftScaled() {
        WitchOvenBlockEntity be = menu.getBlockEntity();
        int burnDuration = be.getBurnDuration();
        int burnProgress = be.getBurnProgress();

        if (burnDuration == 0)
            burnDuration = 200;

        return burnProgress * 13 / burnDuration;
    }

    public int getCookProgressionScaled() {
        WitchOvenBlockEntity be = menu.getBlockEntity();
        int cookProgress = be.getCookProgress();
        int cookDuration = be.getCookDuration();

        return cookDuration != 0 && cookProgress != 0 ? cookProgress * COOK_BAR_WIDTH / cookDuration : 0;
    }

}