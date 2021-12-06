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

package com.favouriteless.enchanted.client.render.tileentity;

import com.favouriteless.enchanted.common.blocks.KettleBlock;
import com.favouriteless.enchanted.common.tileentity.KettleTileEntity;
import com.favouriteless.enchanted.common.tileentity.WitchCauldronTileEntity;
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

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class KettleRenderer extends TileEntityRenderer<KettleTileEntity> {

    public static final ResourceLocation WATER_TEXTURE = new ResourceLocation("minecraft:textures/block/water_still.png");
    private static final int FRAME_TIME = 2;


    public KettleRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(KettleTileEntity kettle, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int combinedLight, int combinedOverlay) {

        long ticks = Minecraft.getInstance().player.level.getGameTime(); // This frame count should be common across all TEs

        int waterAmount = kettle.getWater();
        if(waterAmount > 0) {
            double startingHeight = (kettle.getLevel().getBlockState(kettle.getBlockPos()).getValue(KettleBlock.TYPE) == 1) ? 0.1875D : 0.0625D;
            double waterQuadHeight = startingHeight + (0.25D * (kettle.getWater() / 1000D));

            matrixStack.pushPose();
            matrixStack.translate(0.5D, waterQuadHeight, 0.5D);

            IVertexBuilder vertexBuilder = renderBuffer.getBuffer((RenderType.entityTranslucentCull(WATER_TEXTURE)));
            long time = System.currentTimeMillis() - kettle.startTime;
            KettleQuad.render(matrixStack.last(), vertexBuilder,
                    kettle.getRed(time), kettle.getGreen(time), kettle.getBlue(time), 160,
                    0F, 1/32F * ( (float)(ticks/FRAME_TIME) % 32),
                    combinedLight);

            matrixStack.popPose();
        }
    }

    public static class KettleQuad {

        private static final Vector3f[] positions  =new Vector3f[] {new Vector3f(3.99F, 0.0F, -3.99F), new Vector3f(-3.99F, 0.0F, -3.99F), new Vector3f(-3.99F, 0.0F, 3.99F), new Vector3f(3.99F, 0.0F, 3.99F)};
        private static final Vector2f[] uvs = new Vector2f[] {new Vector2f(1F, 0F), new Vector2f(0F, 0F), new Vector2f(0F, 1/32F), new Vector2f(1F, 1/32F) };

        public static void render(MatrixStack.Entry pPose, IVertexBuilder pVertexBuilder, int red, int green, int blue, int alpha, float uOffset, float vOffset, int combinedLight) {
            Matrix4f poseMatrix = pPose.pose();
            for(int i = 0; i < 4; i++) {
                Vector3f localPos = positions[i];
                Vector2f quadUvs = uvs[i];

                Vector4f posVector = new Vector4f(localPos.x()/16.0F, localPos.y()/16.0F, localPos.z()/16.0F, 1.0F);
                posVector.transform(poseMatrix);

                pVertexBuilder.vertex(posVector.x(), posVector.y(), posVector.z(),
                        red/255F, green/255F, blue/255F, alpha/255F,
                        quadUvs.x + uOffset, quadUvs.y + vOffset,
                        OverlayTexture.NO_OVERLAY, combinedLight,
                        0F, 1F, 0F);
            }
        }

    }
}
