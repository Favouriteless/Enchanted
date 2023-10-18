package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.types.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.binding.RiteBindingFamiliar;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class BindFamiliarSeedParticle extends NoRenderParticle {

	private static final double RADIUS = 4.5D;
	public static final double ORB_RADIUS = 0.7D;

	protected BindFamiliarSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.lifetime = 300;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		if(age++ < lifetime) {
			if(age < 200) {
				spawnParticle();
				spawnParticle();
			}
		}
		else
			remove();
	}

	private void spawnParticle() {
		double cx = Enchanted.RANDOM.nextGaussian();
		double cy = Enchanted.RANDOM.nextGaussian();
		double cz = Enchanted.RANDOM.nextGaussian();
		double c = Math.cbrt(Math.random());
		Vec3 pos = new Vec3(cx, cy, cz).normalize().scale(c * RADIUS).add(x, y, z);

		int fallTicks = RiteBindingFamiliar.BIND_TICKS - age;
		if(Enchanted.RANDOM.nextFloat() < 0.3F)
			fallTicks = Enchanted.RANDOM.nextInt(fallTicks);

		level.addParticle(new DelayedActionData(EnchantedParticles.BIND_FAMILIAR.get(), x, y, z, fallTicks), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new BindFamiliarSeedParticle(level, x, y, z);
		}
	}

}
