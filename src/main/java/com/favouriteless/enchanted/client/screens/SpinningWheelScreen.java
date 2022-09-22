/*
 * Copyright (c) 2021. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.client.screens;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.containers.SpinningWheelContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class SpinningWheelScreen extends ContainerScreen<SpinningWheelContainer> {

    private SpinningWheelContainer container;

    private static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/gui/spinning_wheel.png");

    public static final int BAR0_XPOS = 1;
    public static final int BAR0_YPOS = 1;
    public static final int BAR1_XPOS = 1;
    public static final int BAR1_YPOS = 1;
    public static final int BAR_ICON_U = 176;
    public static final int BAR0_ICON_V = 0;
    public static final int BAR1_ICON_V = 20;
    public static final int BAR_HEIGHT = 20;

    public SpinningWheelScreen(SpinningWheelContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;

        imageWidth = 176;
        imageHeight = 166;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

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


//        this.blit(matrixStack, this.leftPos + COOK_BAR_XPOS, this.topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, (float)(this.imageWidth / 2 - this.font.width(title) / 2), (float)this.titleLabelY, Color.DARK_GRAY.getRGB());
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, Color.DARK_GRAY.getRGB());
    }

}