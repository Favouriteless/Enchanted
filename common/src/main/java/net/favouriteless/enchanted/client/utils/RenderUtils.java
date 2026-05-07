package net.favouriteless.enchanted.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.favouriteless.enchanted.common.util.ColourUtils.ARGB;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void quad(PoseStack pose, VertexConsumer buffer, TextureAtlasSprite sprite, float apothem,
                            ARGB colour, int packedLight) {
        quad(pose, buffer, sprite, apothem, colour.a(), colour.r(), colour.g(), colour.b(), packedLight);
    }

    public static void quad(PoseStack pose, VertexConsumer buffer, TextureAtlasSprite sprite, float apothem,
                            int a, int r, int g, int b, int packedLight) {
        Matrix4f p = pose.last().pose();
        quadVertex(buffer, p, apothem, 0, -apothem, r, g, b, a, sprite.getU((0.5F + apothem)), sprite.getV((0.5F - apothem)), packedLight);
        quadVertex(buffer, p, -apothem, 0, -apothem, r, g, b, a, sprite.getU((0.5F - apothem)), sprite.getV((0.5F - apothem)), packedLight);
        quadVertex(buffer, p, -apothem, 0, apothem, r, g, b, a, sprite.getU((0.5F - apothem)), sprite.getV((0.5F + apothem)), packedLight);
        quadVertex(buffer, p, apothem, 0, apothem, r, g, b, a, sprite.getU((0.5F + apothem)), sprite.getV((0.5F + apothem)), packedLight);
    }

    private static void quadVertex(VertexConsumer consumer, Matrix4f poseMatrix, float x, float y, float z, int red, int green, int blue, int alpha, float u, float v, int packedLight) {
        consumer.addVertex(poseMatrix, x, y, z).setColor(red, green, blue, alpha).setUv(u, v).setLight(packedLight).setNormal(0, 1, 0);
    }

}
