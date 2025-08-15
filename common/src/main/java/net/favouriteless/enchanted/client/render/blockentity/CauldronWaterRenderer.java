package net.favouriteless.enchanted.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.favouriteless.enchanted.common.blocks.cauldrons.CauldronBlockBase;
import net.favouriteless.enchanted.common.blocks.entity.CauldronBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import javax.swing.plaf.metal.MetalTheme;

public class CauldronWaterRenderer<T extends CauldronBlockEntity<?>> implements BlockEntityRenderer<T> {

    public static final ResourceLocation WATER_TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still");

    private final float apothem;

    public CauldronWaterRenderer(float width) {
        this.apothem = width / 32;
    }

    @Override
    public void render(T be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = be.getLevel().getBlockState(be.getBlockPos());
        if(state.getBlock() instanceof CauldronBlockBase) {
            if(be.getWater() > 0) {
                VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
                TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(WATER_TEXTURE);

                long time = System.currentTimeMillis() - be.startTime;
                int r = be.getRed(time);
                int g = be.getGreen(time);
                int b = be.getBlue(time);
                int a = 160;

                poseStack.pushPose();
                poseStack.translate(0.5D, be.getWaterY(state), 0.5D);
                Matrix4f p = poseStack.last().pose();


                vertex(buffer, p, apothem, 0, -apothem, r, g, b, a, sprite.getU((0.5F + apothem)), sprite.getV((0.5F - apothem)), packedLight);
                vertex(buffer, p, -apothem, 0, -apothem, r, g, b, a, sprite.getU((0.5F - apothem)), sprite.getV((0.5F - apothem)), packedLight);
                vertex(buffer, p, -apothem, 0, apothem, r, g, b, a, sprite.getU((0.5F - apothem)), sprite.getV((0.5F + apothem)), packedLight);
                vertex(buffer, p, apothem, 0, apothem, r, g, b, a, sprite.getU((0.5F + apothem)), sprite.getV((0.5F + apothem)), packedLight);

                poseStack.popPose();
            }
        }
    }

    private void vertex(VertexConsumer consumer, Matrix4f poseMatrix, float x, float y, float z, int red, int green, int blue, int alpha, float u, float v, int packedLight) {
        consumer.addVertex(poseMatrix, x, y, z).setColor(red, green, blue, alpha).setUv(u, v).setLight(packedLight).setNormal(0, 1, 0);
    }

}
