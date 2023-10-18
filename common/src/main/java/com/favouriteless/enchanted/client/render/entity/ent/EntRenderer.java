package com.favouriteless.enchanted.client.render.entity.ent;

import com.favouriteless.enchanted.client.render.model.EntModel;
import com.favouriteless.enchanted.common.entities.Ent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class EntRenderer extends GeoEntityRenderer<Ent> {

    public EntRenderer(Context context) {
        super(context, new EntModel());
    }

    @Override
    public RenderType getRenderType(Ent ent, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

}

