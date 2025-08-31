package net.favouriteless.enchanted.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.menus.AltarMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class AltarScreen extends AbstractContainerScreen<AltarMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.id("textures/gui/menus/altar.png");

    public AltarScreen(AltarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 88;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float xMouse, int yMouse, int partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int edgeSpacingX = (width - imageWidth) / 2;
        int edgeSpacingY = (height - imageHeight) / 2;
        gui.blit(TEXTURE, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics gui, int xMouse, int yMouse, float partialTicks) {
        renderBackground(gui, xMouse, yMouse, partialTicks);
        super.render(gui, xMouse, yMouse, partialTicks);
    }

    @Override
    protected void renderLabels(GuiGraphics gui, int x, int y) {
        String powerString = String.format("%s/%s (%.2fx)", menu.getCurrentPower(), menu.getMaxPower(), menu.getRechargeMultiplier());
        int xOffset = font.width(powerString) / 2;
        int yOffset = font.lineHeight / 2;
        gui.drawString(font, powerString, imageWidth / 2 - xOffset, imageHeight / 2 - yOffset, Color.darkGray.getRGB(), false);
    }

}
