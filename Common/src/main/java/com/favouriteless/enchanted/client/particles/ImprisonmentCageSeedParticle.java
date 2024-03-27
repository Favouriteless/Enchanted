package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.entity.RiteImprisonment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class ImprisonmentCageSeedParticle extends NoRenderParticle {

	protected ImprisonmentCageSeedParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a++) {
			double angle = Math.toRadians(a);
			double cx = x + Math.sin(angle)*(RiteImprisonment.TETHER_RANGE + 0.3D);
			double cy = y;
			double cz = z + Math.cos(angle)*(RiteImprisonment.TETHER_RANGE + 0.3D);

			level.addParticle(EnchantedParticles.IMPRISONMENT_CAGE.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
			level.addParticle(EnchantedParticles.IMPRISONMENT_CAGE.get(), cx, cy + 4.0D, cz, 0.0D, 0.0D, 0.0D);

			if(a % 20 == 0) {
				for(int i = 0; i < 40; i++) {
					cy += 4.0D / 40;
					level.addParticle(EnchantedParticles.IMPRISONMENT_CAGE.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
				}
			}
		}
		remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ImprisonmentCageSeedParticle(level, x, y, z);
		}
	}
}
