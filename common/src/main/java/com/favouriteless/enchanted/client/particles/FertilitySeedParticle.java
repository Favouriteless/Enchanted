package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class FertilitySeedParticle extends NoRenderParticle {

	protected FertilitySeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a+=2) {
			double cx = Enchanted.RANDOM.nextGaussian();
			double cy = Enchanted.RANDOM.nextGaussian();
			double cz = Enchanted.RANDOM.nextGaussian();;
			Vec3 pos = new Vec3(cx, cy, cz).normalize().scale(0.1D).add(x, y, z);

			level.addParticle(new CircleMagicData(EnchantedParticleTypes.FERTILITY.get(), 255, 255, 255, x, y, z, 0.1D), pos.x, pos.z, pos.y, 0.0D, 0.0D, 0.0D);
		}
		remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new FertilitySeedParticle(level, x, y, z);
		}
	}

}
