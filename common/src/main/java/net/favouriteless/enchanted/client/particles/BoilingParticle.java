package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.Enchanted;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class BoilingParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected BoilingParticle(ClientLevel level, double x, double y, double z, float red, float green, float blue, SpriteSet sprites) {
        super(level, x, y, z);
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.sprites = sprites;
        this.scale(Enchanted.RANDOM.nextFloat() * 0.4F);
        this.lifetime = Enchanted.RANDOM.nextInt(10) + 5;
        this.setSpriteFromAge(sprites);
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        if(age++ >= lifetime)
            remove();
        else
            setSpriteFromAge(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Factory implements ParticleProvider<ColourOptions> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColourOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BoilingParticle(level, x, y, z, data.getRed(), data.getGreen(), data.getBlue(), sprites);
        }
    }

}
