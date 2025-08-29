package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.common.enchanted.circle_magic.rites.BroilingRite;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class BroilingSeedParticle extends NoRenderParticle {

	protected BroilingSeedParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a+=6) {
			float angle = a * Mth.DEG_TO_RAD;
			double cx = xo + Mth.sin(angle) * BroilingRite.CIRCLE_RADIUS;
			double cz = zo + Mth.cos(angle) * BroilingRite.CIRCLE_RADIUS;

			level.addParticle(ParticleTypes.FLAME, cx, yo, cz, 0.0D, 0.0D, 0.0D);
		}
		this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {}

		@Override
		public Particle createParticle(SimpleParticleType data, ClientLevel world, double x, double y, double z,
									   double xSpeed, double ySpeed, double zSpeed) {
			return new BroilingSeedParticle(world, x, y, z);
		}

	}

}
