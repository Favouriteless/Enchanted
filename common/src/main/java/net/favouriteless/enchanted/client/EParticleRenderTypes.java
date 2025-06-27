package net.favouriteless.enchanted.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.favouriteless.enchanted.integrations.IrisIntegrations;
import net.favouriteless.enchanted.platform.CommonServices;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;

public class EParticleRenderTypes {

    public static final ParticleRenderType PARTICLE_TRANSLUCENT = new ParticleRenderType() {

        @Override
        public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
            RenderSystem.depthMask(true);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(() -> EShaders.PARTICLE_NO_CUTOFF);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return "ENCHANTED_TRANSLUCENT";
        }

    };

    public static ParticleRenderType translucentParticle() {
        if(CommonServices.PLATFORM.isModLoaded("iris") && IrisIntegrations.isShaderpackLoaded())
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        return PARTICLE_TRANSLUCENT;
    }

}
