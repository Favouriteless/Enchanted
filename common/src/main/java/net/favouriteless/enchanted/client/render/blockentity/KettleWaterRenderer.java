package net.favouriteless.enchanted.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.favouriteless.enchanted.client.utils.RenderUtils;
import net.favouriteless.enchanted.common.blocks.entity.KettleBlockEntity;
import net.favouriteless.enchanted.common.util.ColourUtils.ARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;

public class KettleWaterRenderer implements BlockEntityRenderer<KettleBlockEntity> {

    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("block/water_still");
    private static final float APOTHEM = 6 / 32F; // 6 pixels across, / 16 / 2
    private static final float WATER_START = 0.0625F;
    private static final float WATER_END = 0.3125F;

    public KettleWaterRenderer(Context ctx) {}

    @Override
    public void render(KettleBlockEntity be, float partialTicks, PoseStack pose, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(be.getFluidAmount() == 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucent());
        TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
        ARGB colour = be.getColour(mc.level.getGameTime() + partialTicks);

        pose.pushPose();
        pose.translate(0.5D, Mth.lerp(be.getFluidAmount() / (float)be.getFluidCapacity(), WATER_START, WATER_END), 0.5D);
        RenderUtils.quad(pose, buffer, sprite, APOTHEM, colour, packedLight);
        pose.popPose();
    }

}
