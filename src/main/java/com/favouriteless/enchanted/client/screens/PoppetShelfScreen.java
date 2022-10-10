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
import com.favouriteless.enchanted.common.containers.PoppetShelfContainer;
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
public class PoppetShelfScreen extends ContainerScreen<PoppetShelfContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Enchanted.MOD_ID, "textures/gui/poppet_shelf.png");

    public PoppetShelfScreen(PoppetShelfContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 131;
        this.inventoryLabelY = this.imageHeight - 94; // This is set in super constructor
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int pX, int pY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(TEXTURE);

        int edgeSpacingX = (this.width - this.imageWidth) / 2;
        int edgeSpacingY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int pX, int pY) {
        font.draw(matrixStack, title, (float)(imageWidth / 2 - font.width(title) / 2), (float)titleLabelY, Color.DARK_GRAY.getRGB());
    }

}
