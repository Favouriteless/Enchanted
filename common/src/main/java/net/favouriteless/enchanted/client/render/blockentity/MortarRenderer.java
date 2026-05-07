package net.favouriteless.enchanted.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.favouriteless.enchanted.client.utils.RenderUtils;
import net.favouriteless.enchanted.common.blocks.entity.MortarBlockEntity;
import net.favouriteless.enchanted.common.util.ColourUtils;
import net.favouriteless.enchanted.common.util.ColourUtils.ARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MortarRenderer implements BlockEntityRenderer<MortarBlockEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still");
    private static final float APOTHEM = 6 / 32F; // 6 pixels across, / 16 / 2
    private static final float QUAD_START = 0.125F;
    private static final float QUAD_END = 0.28125F;

    private final ItemRenderer itemRenderer;

    public MortarRenderer(Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(MortarBlockEntity be, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack input = be.getInput();
        if(input != null)
            renderItems(input, pose, bufferSource, packedLight, packedOverlay);

        if(be.getProgress() != 0)
            renderPaste(be, pose, bufferSource, packedLight);
    }

    private void renderItems(ItemStack stack, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack single = stack.copyWithCount(1);

        pose.pushPose();
        pose.translate(0.5F, QUAD_START, 0.5F);
        pose.scale(0.35F, 0.35F, 0.35F);
        pose.mulPose(Axis.XP.rotationDegrees(90.0F));

        for(int i = 0; i < stack.getCount(); i++) {
            pose.pushPose();
            pose.translate(0.0F, 0.0F, -i * 0.0625F); // Use Z axis because the pose is rotated 90 degrees
            pose.mulPose(Axis.ZP.rotationDegrees(45.0F * i));
            itemRenderer.renderStatic(single, ItemDisplayContext.FIXED, packedLight, packedOverlay, pose, bufferSource, null, 0);
            pose.popPose();
        }

        pose.popPose();
    }

    private void renderPaste(MortarBlockEntity be, PoseStack pose, MultiBufferSource bufferSource, int packedLight) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.solid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
        ARGB colour = ColourUtils.intToARGB(be.getColour());

        pose.pushPose();
        pose.translate(0.5D, Mth.lerp(be.getProgress() / (float)be.getMaxProgress(), QUAD_START, QUAD_END), 0.5D);
        RenderUtils.quad(pose, buffer, sprite, APOTHEM, colour, packedLight);
        pose.popPose();
    }

}
