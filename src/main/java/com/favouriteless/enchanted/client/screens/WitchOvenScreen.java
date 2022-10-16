/*
 * Copyright (c) 2022. Favouriteless
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
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class WitchOvenScreen extends AbstractContainerScreen<WitchOvenContainer> {

    private final WitchOvenContainer container;

    private static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/gui/witch_oven.png");

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

    public WitchOvenScreen(WitchOvenContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.container = container;

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }


    protected void renderHoveredTooltip(PoseStack matrixStack, int mouseX, int mouseY) {
        if (!minecraft.player.inventory.getCarried().isEmpty()) return;
        super.renderTooltip(matrixStack, mouseX, mouseY);
    }


    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);

        int edgeSpacingX = (width - imageWidth) / 2;
        int edgeSpacingY = (height - imageHeight) / 2;
        blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, imageWidth, imageHeight);

        int cookProgressionScaled = getCookProgressionScaled();
        blit(matrixStack, leftPos + COOK_BAR_XPOS, topPos + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V, cookProgressionScaled + 1, COOK_BAR_HEIGHT);


        // Draw fuel remaining bar
        if (this.container.getData().get(0) > 0) {
            int burnLeftScaled = getBurnLeftScaled();
            blit(matrixStack, leftPos + FLAME_XPOS, topPos + FLAME_YPOS + 12 - burnLeftScaled, FLAME_ICON_U, FLAME_ICON_V - burnLeftScaled, FLAME_SIZE, burnLeftScaled + 1);
        }

    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        font.draw(matrixStack, title, (float)(imageWidth / 2 - font.width(title) / 2), (float)titleLabelY, Color.DARK_GRAY.getRGB());
        font.draw(matrixStack, inventory.getDisplayName(), (float)inventoryLabelX, (float)inventoryLabelY, Color.DARK_GRAY.getRGB());
    }

    public int getBurnLeftScaled() {
        int maxBurnTime = container.getData().get(1);
        int burnTime = container.getData().get(0);
        if (maxBurnTime == 0) {
            maxBurnTime = 200;
        }

        return burnTime * 13 / maxBurnTime;
    }

    public int getCookProgressionScaled() {
        int cookTime = container.getData().get(2);
        int cookTimeTotal = container.getData().get(3);
        return cookTimeTotal != 0 && cookTime != 0 ? cookTime * COOK_BAR_WIDTH / cookTimeTotal : 0;
    }

}