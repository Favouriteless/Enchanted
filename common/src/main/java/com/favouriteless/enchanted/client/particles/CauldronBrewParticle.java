package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nullable;

public class CauldronBrewParticle extends TextureSheetParticle {

    protected CauldronBrewParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int red, int green, int blue) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min((red + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);
        this.gCol = Math.min((green + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);
        this.bCol = Math.min((blue + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);

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
            CauldronBrewParticle particle = new CauldronBrewParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }

}
