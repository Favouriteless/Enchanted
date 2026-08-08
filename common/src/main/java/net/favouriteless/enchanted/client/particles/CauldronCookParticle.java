package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.init.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.ColourOptions;
import net.favouriteless.enchanted.common.util.RandomUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class CauldronCookParticle extends TextureSheetParticle {

    public static final double ANGLE = 8.0D;
    public static final double RADIUS_INCREASE = 0.005D;

    private final int circleStart;
    private final double xStart;
    private final double zStart;

    private double currentRadius;

    protected CauldronCookParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
                                   double zSpeed, float red, float green, float blue) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min(red + (RandomUtils.nextInt(40) - 20) / 255F, 1.0F);
        this.gCol = Math.min(green + (RandomUtils.nextInt(40) - 20) / 255F, 1.0F);
        this.bCol = Math.min(blue + (RandomUtils.nextInt(40) - 20) / 255F, 1.0F);

        this.scale(random.nextFloat() * 0.6F);
        this.age = 0;
        this.lifetime = 80;
        this.hasPhysics = false;
        this.circleStart = RandomUtils.nextInt(5) + 10;

        this.xStart = x;
        this.zStart = z;
        // Create random initial velocity
        this.xd = (Math.random() * 0.005D) - 0.0025D;
        this.yd = 0.04D;
        this.zd = (Math.random() * 0.005D) - 0.0025D;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) {
            alpha -= 0.1F;
            if (alpha <= 0) {
                remove();
            }
        }

        if (age >= circleStart) {
            if (age == circleStart) { // Just started rotating
                xd = 0;
                yd = 0;
                zd = 0;
                double xOffset = x - xStart;
                double zOffset = z - zStart;
                currentRadius = Math.sqrt((xOffset * xOffset) + (zOffset * zOffset));
            }

            currentRadius += RADIUS_INCREASE;
            double angle = Math.toRadians(ANGLE);
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            Vec3 newPos = new Vec3(
                    cos * (x - xStart) - sin * (z - zStart),
                    0,
                    sin * (x - xStart) + cos * (z - zStart)
            ).normalize().scale(currentRadius).add(xStart, 0.0D, zStart);

            x = newPos.x;
            z = newPos.z;
        }
        move(xd, yd, zd);
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
            CauldronCookParticle particle = new CauldronCookParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }

}
