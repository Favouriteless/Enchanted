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
import com.favouriteless.enchanted.common.containers.WitchOvenContainer;
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
public class WitchOvenScreen extends ContainerScreen<WitchOvenContainer> {

    private WitchOvenContainer container;

    // This is the resource location for the background image
    private static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/gui/witch_oven.png");

    // some [x,y] coordinates of graphical elements
    public static final int COOK_BAR_XPOS = 76;
    public static final int COOK_BAR_YPOS = 19;
    public static final int COOK_BAR_ICON_U = 176;
    public static final int COOK_BAR_ICON_V = 14;
    public static final int COOK_BAR_WIDTH = 24;
    public static final int COOK_BAR_HEIGHT = 17;

    public static final int FLAME_XPOS = 80;
    public static final int FLAME_YPOS = 33;
    public static final int FLAME_ICON_U = 176;
    public static final int FLAME_ICON_V = 12;
    public static final int FLAME_SIZE = 14;

    public WitchOvenScreen(WitchOvenContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;

        // Should match the size of the texture!
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    // Draw the Tool tip text if hovering over something of interest on the screen
    protected void renderHoveredTooltip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (!this.minecraft.player.inventory.getCarried().isEmpty()) return;  // no tooltip if the player is dragging something
        super.renderTooltip(matrixStack, mouseX, mouseY);
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


        // Draw fuel remaining bar
        if (this.container.isBurning()) {
            int burnLeftScaled = this.container.getBurnLeftScaled();
            this.blit(matrixStack, this.leftPos + FLAME_XPOS, this.topPos + FLAME_YPOS + 12 - burnLeftScaled, FLAME_ICON_U, FLAME_ICON_V - burnLeftScaled, FLAME_SIZE, burnLeftScaled + 1);
        }

    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, (float)(this.imageWidth / 2 - this.font.width(title) / 2), (float)this.titleLabelY, Color.DARK_GRAY.getRGB());
        this.font.draw(matrixStack, this.inventory.getDisplayName(), (float)this.inventoryLabelX, (float)this.inventoryLabelY, Color.DARK_GRAY.getRGB());
    }
}