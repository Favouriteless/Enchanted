package com.favouriteless.enchanted.client.render.blockentity;

import com.favouriteless.enchanted.common.blockentities.PoppetShelfBlockEntity;
import com.favouriteless.enchanted.common.poppet.PoppetShelfInventory;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class PoppetShelfRenderer implements BlockEntityRenderer<PoppetShelfBlockEntity> {

    private static final Vector3f[] ITEM_POS = new Vector3f[] {
            new Vector3f(0.3125F, 0.515F, 0.3125F),
            new Vector3f(0.6875F, 0.515F, 0.3125F),
            new Vector3f(0.6875F, 0.515F, 0.6875F),
            new Vector3f(0.3125F, 0.515F, 0.6875F)
    };

    public PoppetShelfRenderer(Context context) {

    }

    @Override
    public void render(PoppetShelfBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        PoppetShelfInventory inventory = blockEntity.getInventory();
        for(int i = 0; i < inventory.size(); i++) {
            ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
            poseStack.pushPose();
            poseStack.translate(ITEM_POS[i].x(), ITEM_POS[i].y(), ITEM_POS[i].z());
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(90 * i));
            poseStack.scale(0.3F, 0.3F, 0.3F);
            renderer.renderStatic(inventory.get(i), TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
            poseStack.popPose();
        }
    }


}
