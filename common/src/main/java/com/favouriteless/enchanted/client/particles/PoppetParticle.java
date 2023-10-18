package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.types.TwoToneColouredParticleType.TwoToneColouredData;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;

import javax.annotation.Nullable;

public class PoppetParticle extends SimpleAnimatedParticle {

    protected PoppetParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites, int r, int g, int b, int r1, int g1, int b1) {
        super(level, x, y, z, sprites, -0.05F);
        this.friction = 0.6F;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= 0.75F;
        this.lifetime = 60 + this.random.nextInt(12);
        this.setSpriteFromAge(sprites);

        if (this.random.nextInt(4) == 0) {
            this.setColor(r1/255F, g1/255F, b1/255F);
        } else {
            this.setColor(r / 255F, g / 255F, b / 255F);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<TwoToneColouredData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(TwoToneColouredData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PoppetParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, data.getRed(), data.getGreen(), data.getBlue(), data.getRed1(), data.getGreen1(), data.getBlue1());
        }
    }

}
