package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.particles.types.ColouredCircleOptions;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class BlightSeedParticle extends NoRenderParticle {

    protected BlightSeedParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void tick() {
        for (int a = 0; a < 360; a += 2) {
            double angle = Math.toRadians(a);
            double cx = x + Math.sin(angle) * 0.1D;
            double cz = z + Math.cos(angle) * 0.1D;

            level.addParticle(
                    new ColouredCircleOptions(
                            EParticleTypes.BLIGHT.get(), 0x1F1E4D,
                            new Vec3(x, y, z), 8
                    ), cx, y, cz, 0, 0, 0
            );
        }
        this.remove();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Factory(SpriteSet sprites) {
        }

        @Override
        public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new BlightSeedParticle(level, x, y, z);
        }

    }

}
