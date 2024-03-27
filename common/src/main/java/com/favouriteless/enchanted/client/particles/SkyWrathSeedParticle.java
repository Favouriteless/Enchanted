package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.types.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import com.favouriteless.enchanted.common.rites.world.RiteSkyWrath;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SkyWrathSeedParticle extends NoRenderParticle {

	private static final double xSpread = 0.3D;
	private static final double ySpread = 0.3D;
	private static final double zSpread = 0.3D;

	protected SkyWrathSeedParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.lifetime = 60;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		if(age++ < lifetime) {
			if(age % 3 == 0) {
				for(int a = 0; a < 360; a += 20) {
					double angle = Math.toRadians(a);

					double cx = x + Math.sin(angle) * RiteSkyWrath.LIGHTNING_RADIUS + Math.random() * xSpread;
					double cy = y + Math.random() * ySpread;
					double cz = z + Math.cos(angle) * RiteSkyWrath.LIGHTNING_RADIUS + Math.random() * zSpread;

					level.addParticle(new DelayedActionData(EnchantedParticleTypes.SKY_WRATH.get(), x, y, z, RiteSkyWrath.EXPLODE-age), cx, cy, cz, 0, 0, 0);
				}
			}
		}
		else
			this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new SkyWrathSeedParticle(level, x, y, z);
		}
	}

}
