package com.favouriteless.enchanted.client.screens;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.menus.AltarMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;

public class AltarScreen extends AbstractContainerScreen<AltarMenu> {

    private static final ResourceLocation TEXTURE = Enchanted.location("textures/gui/altar.png");

    public AltarScreen(AltarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 88;
    }

    @Override
    public void render(PoseStack poseStack, int xMouse, int yMouse, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, xMouse, yMouse, partialTicks);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        String powerString = String.format("%s/%s (%sx)", menu.getCurrentPower(), menu.getMaxPower(), menu.getRechargeMultiplier());
        int xOffset = this.font.width(powerString) / 2;
        int yOffset = this.font.lineHeight / 2;
        this.font.draw(poseStack, powerString, (float)this.imageWidth / 2 - xOffset, (float)this.imageHeight / 2 - yOffset, Color.darkGray.getRGB());
    }

}
