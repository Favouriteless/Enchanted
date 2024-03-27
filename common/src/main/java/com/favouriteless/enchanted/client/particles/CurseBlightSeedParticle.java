package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class CurseBlightSeedParticle extends NoRenderParticle {

	protected CurseBlightSeedParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a+=2) {
			double angle = Math.toRadians(a);
			double cx = x + Math.sin(angle) * 0.1D;
			double cz = z + Math.cos(angle) * 0.1D;

			level.addParticle(new CircleMagicData(EnchantedParticleTypes.CURSE_BLIGHT.get(), 31, 30, 77, x, y, z, 0.1D), cx, y, cz, 0.0D, 0.0D, 0.0D);
		}
		this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CurseBlightSeedParticle(level, x, y, z);
		}
	}

}
