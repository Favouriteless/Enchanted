package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nullable;

public class BoilingParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected BoilingParticle(ClientLevel level, double x, double y, double z, int red, int green, int blue, SpriteSet sprites) {
        super(level, x, y, z);
        this.rCol = red/255F;
        this.gCol = green/255F;
        this.bCol = blue/255F;
        this.sprites = sprites;
        this.scale(Enchanted.RANDOM.nextFloat() * 0.4F);
        this.lifetime = Enchanted.RANDOM.nextInt(10) + 5;
        this.setSpriteFromAge(sprites);
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        if (age++ >= lifetime)
            remove();
        else
            setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleColouredParticleType.SimpleColouredData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleColouredParticleType.SimpleColouredData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BoilingParticle(level, x, y, z, data.getRed(), data.getGreen(), data.getBlue(), sprites);
        }
    }

}
