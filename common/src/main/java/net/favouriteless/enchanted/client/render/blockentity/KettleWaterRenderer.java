package net.favouriteless.enchanted.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.common.util.ColourUtils.ARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix4f;

public class KettleWaterRenderer<T extends KettleBlockEntity> implements BlockEntityRenderer<T> {

    public static final ResourceLocation WATER_TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still");

    private final float apothem;
    private final float waterStart;
    private final float waterEnd;

    public KettleWaterRenderer(float width, float waterStart, float waterEnd) {
        this.apothem = width / 32;
        this.waterStart = waterStart;
        this.waterEnd = waterEnd;
    }

    @Override
    public void render(T be, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(be.getFluidAmount() == 0)
            return;

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(WATER_TEXTURE);

        ARGB colour = be.getColour(EnchantedClient.getGameTime());
        int a = colour.a();
        int r = colour.r();
        int g = colour.g();
        int b = colour.b();

        float f = be.getFluidAmount() / (float)be.getFluidCapacity();

        pose.pushPose();
        pose.translate(0.5D, Mth.lerp(f, waterStart, waterEnd), 0.5D);

        Matrix4f p = pose.last().pose();
        vertex(buffer, p, apothem, 0, -apothem, r, g, b, a, sprite.getU((0.5F + apothem)), sprite.getV((0.5F - apothem)), packedLight);
        vertex(buffer, p, -apothem, 0, -apothem, r, g, b, a, sprite.getU((0.5F - apothem)), sprite.getV((0.5F - apothem)), packedLight);
        vertex(buffer, p, -apothem, 0, apothem, r, g, b, a, sprite.getU((0.5F - apothem)), sprite.getV((0.5F + apothem)), packedLight);
        vertex(buffer, p, apothem, 0, apothem, r, g, b, a, sprite.getU((0.5F + apothem)), sprite.getV((0.5F + apothem)), packedLight);

        pose.popPose();
    }

    private void vertex(VertexConsumer consumer, Matrix4f poseMatrix, float x, float y, float z, int red, int green, int blue, int alpha, float u, float v, int packedLight) {
        consumer.addVertex(poseMatrix, x, y, z).setColor(red, green, blue, alpha).setUv(u, v).setLight(packedLight).setNormal(0, 1, 0);
    }

}
