package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.TwoColourOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class PoppetParticle extends SimpleAnimatedParticle {

    protected PoppetParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
                             double zSpeed, SpriteSet sprites, float r, float g, float b, float r1, float g1, float b1) {
        super(level, x, y, z, sprites, -0.05F);
        this.friction = 0.6F;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= 0.75F;
        this.lifetime = 60 + this.random.nextInt(12);
        this.setSpriteFromAge(sprites);

        if(this.random.nextInt(4) == 0)
            this.setColor(r1, g1, b1);
        else
            this.setColor(r, g, b);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EParticleRenderTypes.translucentParticle();
    }

    public static class Factory implements ParticleProvider<TwoColourOptions> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(TwoColourOptions data, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new PoppetParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites,
                    data.getRedFirst(), data.getGreenFirst(), data.getBlueFirst(),
                    data.getRedSecond(), data.getGreenSecond(), data.getBlueSecond());
        }
    }

}
