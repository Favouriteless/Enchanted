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

package com.favouriteless.enchanted.client.render.tileentity;

import com.favouriteless.enchanted.common.tileentity.CauldronTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CauldronWaterRenderer<T extends CauldronTileEntity<?>> extends TileEntityRenderer<T> {

    public static final ResourceLocation WATER_TEXTURE = new ResourceLocation("minecraft:textures/block/water_still.png");
    private static final int FRAME_TIME = 2;
    private final CauldronQuad quad;


    public CauldronWaterRenderer(TileEntityRendererDispatcher dispatcher, int waterWidth) {
        super(dispatcher);
        this.quad = new CauldronQuad((float)waterWidth / 2 - 0.01F);
    }

    @Override
    public void render(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {
        long ticks = Minecraft.getInstance().player.level.getGameTime(); // This frame count should be common across all TEs

        int waterAmount = tileEntity.getWater();
        if(waterAmount > 0) {

            double waterQuadHeight = tileEntity.getWaterY();

            matrixStack.pushPose();
            matrixStack.translate(0.5D, waterQuadHeight, 0.5D);

            IVertexBuilder vertexBuilder = renderBuffer.getBuffer((RenderType.entityTranslucentCull(WATER_TEXTURE)));
            long time = System.currentTimeMillis() - tileEntity.startTime;
            quad.render(matrixStack.last(), vertexBuilder,
                    tileEntity.getRed(time), tileEntity.getGreen(time), tileEntity.getBlue(time), 160,
                    0F, 1/32F * ( (float)(ticks/FRAME_TIME) % 32),
                    combinedLight);

            matrixStack.popPose();
        }
    }

    public static class CauldronQuad {

        private static final Vector2f[] uvs = new Vector2f[] {new Vector2f(1F, 0F), new Vector2f(0F, 0F), new Vector2f(0F, 1/32F), new Vector2f(1F, 1/32F) };
        private final Vector3f[] positions;

        public CauldronQuad(float apothem) {
            positions = new Vector3f[] { new Vector3f(apothem, 0.0F, -apothem), new Vector3f(-apothem, 0.0F, -apothem), new Vector3f(-apothem, 0.0F, apothem), new Vector3f(apothem, 0.0F, apothem) };
        }

        public void render(MatrixStack.Entry pose, IVertexBuilder vertexBuilder, int red, int green, int blue, int alpha, float uOffset, float vOffset, int combinedLight) {
            Matrix4f poseMatrix = pose.pose();
            for(int i = 0; i < 4; i++) {
                Vector3f localPos = positions[i];
                Vector2f quadUvs = uvs[i];

                Vector4f posVector = new Vector4f(localPos.x()/16.0F, localPos.y()/16.0F, localPos.z()/16.0F, 1.0F);
                posVector.transform(poseMatrix);

                vertexBuilder.vertex(posVector.x(), posVector.y(), posVector.z(),
                        red/255F, green/255F, blue/255F, alpha/255F,
                        quadUvs.x + uOffset, quadUvs.y + vOffset,
                        OverlayTexture.NO_OVERLAY, combinedLight,
                        0F, 1F, 0F);
            }
        }

    }
}
