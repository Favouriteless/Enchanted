package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.init.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class KettleCookParticle extends TextureSheetParticle {

    protected KettleCookParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed,
                                 double zSpeed, float red, float green, float blue) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min(red + (RandomUtils.nextInt(20) - 10) / 255F, 1.0F);
        this.gCol = Math.min(green + (RandomUtils.nextInt(20) - 10) / 255F, 1.0F);
        this.bCol = Math.min(blue + (RandomUtils.nextInt(20) - 10) / 255F, 1.0F);

        this.scale(random.nextFloat() * 0.4F);
        this.lifetime = 20;
        this.gravity = 0.02F;
        this.hasPhysics = false;

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (lifetime-- <= 0) {
            alpha -= 0.1F;

            if (alpha <= 0) {
                remove();
            }
        } else {
            yd = -gravity;
            move(xd, yd, zd);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EParticleRenderTypes.translucentParticle();
    }

    public static class Factory implements ParticleProvider<ColourOptions> {

        private final SpriteSet sprites;

        public Factory(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Override
        public Particle createParticle(ColourOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            KettleCookParticle particle = new KettleCookParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
