package com.favouriteless.enchanted.client.render.blockentity;

import com.favouriteless.enchanted.common.blocks.entity.CauldronBlockEntity;
import com.favouriteless.enchanted.common.blocks.cauldrons.CauldronBlockBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;

public class CauldronWaterRenderer<T extends CauldronBlockEntity<?>> implements BlockEntityRenderer<T> {

    public static final ResourceLocation WATER_TEXTURE = new ResourceLocation("minecraft:textures/block/water_still.png");
    private static final int FRAME_TIME = 2;
    private final CauldronQuad quad;


    public CauldronWaterRenderer(int waterWidth) {
        super();
        this.quad = new CauldronQuad((float)waterWidth / 2 - 0.01F);
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource renderBuffer, int combinedLight, int combinedOverlay) {
        BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos());
        if(state.getBlock() instanceof CauldronBlockBase) {
            long ticks = blockEntity.getLevel().getGameTime(); // This frame count should be common across all TEs

            int waterAmount = blockEntity.getWater();
            if(waterAmount > 0) {

                double waterQuadHeight = blockEntity.getWaterY(state);

                poseStack.pushPose();
                poseStack.translate(0.5D, waterQuadHeight, 0.5D);

                VertexConsumer vertexBuilder = renderBuffer.getBuffer((RenderType.entityTranslucentCull(WATER_TEXTURE)));
                long time = System.currentTimeMillis() - blockEntity.startTime;
                quad.render(poseStack.last(), vertexBuilder,
                        blockEntity.getRed(time), blockEntity.getGreen(time), blockEntity.getBlue(time), 160,
                        0F, 1 / 32F * ((float) (ticks / FRAME_TIME) % 32),
                        combinedLight);

                poseStack.popPose();
            }
        }
    }

    public static class CauldronQuad {

        private static final Vec2[] uvs = new Vec2[] {new Vec2(1F, 0F), new Vec2(0F, 0F), new Vec2(0F, 1/32F), new Vec2(1F, 1/32F) };
        private final Vector3f[] positions;

        public CauldronQuad(float apothem) {
            positions = new Vector3f[] { new Vector3f(apothem, 0.0F, -apothem), new Vector3f(-apothem, 0.0F, -apothem), new Vector3f(-apothem, 0.0F, apothem), new Vector3f(apothem, 0.0F, apothem) };
        }

        public void render(PoseStack.Pose pose, VertexConsumer vertexBuilder, int red, int green, int blue, int alpha, float uOffset, float vOffset, int combinedLight) {
            Matrix4f poseMatrix = pose.pose();
            for(int i = 0; i < 4; i++) {
                Vector3f localPos = positions[i];
                Vec2 quadUvs = uvs[i];

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
