package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.particles.types.ColouredCircleOptions;
import net.favouriteless.enchanted.client.particles.types.DoubleOptions;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;

public class TranspositionIronSeedParticle extends NoRenderParticle {

    private final double radius;

    protected TranspositionIronSeedParticle(ClientLevel level, double x, double y, double z, double radius) {
        super(level, x, y, z);
        this.hasPhysics = false;
        this.radius = radius;
    }

    @Override
    public void tick() {
        for (int a = 0; a < 360; a += 2) {
            double cx = x + Math.sin(a) * radius;
            double cz = z + Math.cos(a) * radius;

            level.addParticle(
                    new ColouredCircleOptions(
                            EParticleTypes.CIRCLE_MAGIC.get(), 0xAA6f3A,
                            new Vec3(x, y, z), (float) radius
                    ), cx, y, cz,
                    0.0D, 0.0D, 0.0D
            );
        }
        this.remove();
    }

    public static class Factory implements ParticleProvider<DoubleOptions> {

        public Factory(SpriteSet sprites) {
        }

        @Override
        public Particle createParticle(DoubleOptions data, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new TranspositionIronSeedParticle(level, x, y, z, data.value());
        }

    }

}
