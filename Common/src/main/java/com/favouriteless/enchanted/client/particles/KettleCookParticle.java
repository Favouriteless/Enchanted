package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nullable;

public class KettleCookParticle extends TextureSheetParticle {

    protected KettleCookParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int red, int green, int blue) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min((red + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);
        this.gCol = Math.min((green + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);
        this.bCol = Math.min((blue + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);

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
            
            if(alpha <= 0)
                remove();
        } else {
            yd = -gravity;
            move(xd, yd, zd);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleColouredParticleType.SimpleColouredData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleColouredParticleType.SimpleColouredData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            KettleCookParticle particle = new KettleCookParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
