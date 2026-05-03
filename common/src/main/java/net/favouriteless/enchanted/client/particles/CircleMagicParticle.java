package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.init.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.ColouredCircleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;


public class CircleMagicParticle extends TextureSheetParticle {

    public static final double ANGLE = 2.0D;
    private final float radius;

    private final double xStart;
    private final double zStart;

    protected CircleMagicParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed,
                                  double zSpeed, float red, float green, float blue, Vec3 center, float radius) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.radius = radius;

        this.quadSize = 0.06F;
        this.age = 0;
        this.lifetime = 60;
        this.hasPhysics = false;

        this.xStart = center.x;
        this.zStart = center.z;

        // Create initial velocity
        this.xd = 0;
        this.yd = 0.04D;
        this.zd = 0;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if(age++ >= lifetime) {
            alpha -= 0.01F;
            if(alpha <= 0)
                remove();
        }

        double angle = Math.toRadians(ANGLE);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        Vec3 newPos = new Vec3(
                cos * (x - xStart) - sin * (z - zStart),
                0,
                sin * (x - xStart) + cos * (z - zStart)
        ).normalize().scale(radius).add(xStart, 0, zStart);

        xd = newPos.x - x;
        zd = newPos.z - z;

        move(xd, yd, zd);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EParticleRenderTypes.translucentParticle();
    }

    public record Factory(SpriteSet sprites) implements ParticleProvider<ColouredCircleOptions> {

        @Override
        public Particle createParticle(ColouredCircleOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CircleMagicParticle particle = new CircleMagicParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(),
                    data.getGreen(), data.getBlue(), data.center(), data.radius());
            particle.pickSprite(sprites);
            return particle;
        }

    }
}
