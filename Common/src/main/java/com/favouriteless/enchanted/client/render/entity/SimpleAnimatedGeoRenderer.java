package com.favouriteless.enchanted.client.render.entity;

import com.favouriteless.enchanted.client.render.model.SimpleAnimatedGeoModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SimpleAnimatedGeoRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {

    public SimpleAnimatedGeoRenderer(Context context, String prefix, String name) {
        super(context, new SimpleAnimatedGeoModel<>(prefix, name));
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityCutoutNoCull(texture);
    }

}
