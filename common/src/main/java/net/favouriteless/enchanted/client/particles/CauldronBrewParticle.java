package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class CauldronBrewParticle extends TextureSheetParticle {

    protected CauldronBrewParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float red, float green, float blue) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min(red + (RandomUtils.nextInt(20) - 10)/255F, 1.0F);
        this.gCol = Math.min(green + (RandomUtils.nextInt(20) - 10)/255F, 1.0F);
        this.bCol = Math.min(blue + (RandomUtils.nextInt(20) - 10)/255F, 1.0F);

        this.scale(random.nextFloat() * 0.4F);
        this.lifetime = 200;
        this.gravity = 0.02F;

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
            remove();
        } else {
            yd -= gravity;
            move(xd, yd, zd);
            xd *= 0.93D;
            yd *= 0.93D;
            zd *= 0.93D;

            if(onGround) {
                alpha -= 0.1F;

                if(alpha <= 0)
                    remove();
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EParticleRenderTypes.translucentParticle();
    }

    public static class Factory implements ParticleProvider<ColourOptions> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(ColourOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CauldronBrewParticle particle = new CauldronBrewParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }

}
