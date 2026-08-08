package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.particles.types.DoubleOptions;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;

public class ProtectionSeedParticle extends NoRenderParticle {

    private final float radius;

    protected ProtectionSeedParticle(ClientLevel level, double x, double y, double z, double radius) {
        super(level, x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.hasPhysics = false;
        this.radius = (float) radius;
    }

    @Override
    public void tick() {

        float increment = 1.0f / radius; // Total radians / circumference for radians per step
        for (float y = 0; y <= Mth.PI * 2 + increment / 2; y += increment) {
            for (float p = 0; p <= Mth.PI * 2 + increment / 2; p += increment) {
                if (Math.random() < 0.025D) {
                    float cosP = Mth.cos(p);
                    double cx = Mth.sin(y) * cosP * radius + x + (Math.random() - 0.5d);
                    double cy = Mth.sin(p) * radius + this.y + (Math.random() - 0.5d);
                    double cz = Mth.cos(y) * cosP * radius + z + (Math.random() - 0.5d);
                    level.addParticle(EParticleTypes.PROTECTION.get(), cx, cy, cz, 0, 0, 0);
                }
            }
        }

        if (++age > 20) {
            remove();
        }
    }

    public static class Factory implements ParticleProvider<DoubleOptions> {

        public Factory(SpriteSet sprites) {
        }

        @Override
        public Particle createParticle(DoubleOptions data, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new ProtectionSeedParticle(level, x, y, z, data.value());
        }

    }

}
