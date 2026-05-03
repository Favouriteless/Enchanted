package net.favouriteless.enchanted.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.favouriteless.enchanted.common.blocks.entity.PoppetShelfBlockEntity;
import net.favouriteless.enchanted.common.enchanted.poppet.shelf.PoppetShelfInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

public class PoppetShelfRenderer implements BlockEntityRenderer<PoppetShelfBlockEntity> {

    private static final Vector3f[] ITEM_POS = new Vector3f[] {
            new Vector3f(0.3125F, 0.515F, 0.3125F),
            new Vector3f(0.6875F, 0.515F, 0.3125F),
            new Vector3f(0.6875F, 0.515F, 0.6875F),
            new Vector3f(0.3125F, 0.515F, 0.6875F)
    };

    public PoppetShelfRenderer(Context context) {}

    @Override
    public void render(PoppetShelfBlockEntity shelf, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        PoppetShelfInventory inventory = shelf.getInventory();

        for(int i = 0; i < inventory.getContainerSize(); i++) {
            pose.pushPose();

            pose.translate(ITEM_POS[i].x(), ITEM_POS[i].y(), ITEM_POS[i].z());
            pose.mulPose(Axis.XP.rotationDegrees(90));
            pose.mulPose(Axis.ZP.rotationDegrees(90 * i));
            pose.scale(0.3F, 0.3F, 0.3F);

            itemRenderer.renderStatic(inventory.getItem(i), ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, pose, bufferSource, shelf.getLevel(), 0);

            pose.popPose();
        }
    }


}
