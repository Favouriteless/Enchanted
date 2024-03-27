package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ProtectionSeedParticle extends NoRenderParticle {

	private final double radius;

	protected ProtectionSeedParticle(ClientLevel level, double x, double y, double z, double radius) {
		super(level, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.hasPhysics = false;
		this.radius = radius;
	}

	@Override
	public void tick() {
		double increment = 1.0D / radius; // Total radians / circumference for radians per step
		for(double y = 0; y <= Math.PI*2 + increment/2; y += increment) {
			for(double p = 0; p <= Math.PI*2 + increment/2; p += increment) {
				double cosY = Math.cos(y);
				double sinY = Math.sin(y);
				double cosP = Math.cos(p);
				double sinP = Math.sin(p);
				double cx = sinY * cosP * radius + x + (Math.random()-0.5D);
				double cy = sinP * radius + y + (Math.random()-0.5D);
				double cz = cosY * cosP * radius + z + (Math.random()-0.5D);

				if(Math.random() < 0.5D)
					level.addParticle(EnchantedParticleTypes.PROTECTION.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
			}
		}
		remove();
	}

	public static class Factory implements ParticleProvider<DoubleParticleData> {

		public Factory(SpriteSet sprites) {}

		public Particle createParticle(DoubleParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ProtectionSeedParticle(level, x, y, z, data.getValue());
		}
	}

}
