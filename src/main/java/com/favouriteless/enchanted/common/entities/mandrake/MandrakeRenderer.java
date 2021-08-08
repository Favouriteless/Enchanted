package com.favouriteless.enchanted.common.entities.mandrake;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.util.ResourceLocation;

public class MandrakeRenderer extends MobRenderer<MandrakeEntity, ChickenModel<MandrakeEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/entity/chicken.png");

    public MandrakeRenderer(EntityRendererManager manager) {
        super(manager, new ChickenModel(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MandrakeEntity entity) {
        return TEXTURE;
    }
}
