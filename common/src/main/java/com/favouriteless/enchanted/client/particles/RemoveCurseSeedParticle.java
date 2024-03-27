package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRemoveCurseRite;
import com.favouriteless.enchanted.client.particles.types.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class RemoveCurseSeedParticle extends NoRenderParticle {

	private static final double RADIUS = 4.5D;
	public static final double ORB_RADIUS = 1.0D;

	protected RemoveCurseSeedParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
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
				if(Math.random() < 0.5D)
					spawnParticle();
			}
			if(age > 40) {
				double dx = (Math.random()-0.5D) * ORB_RADIUS/1.5D;
				double dy = (Math.random()-0.5D) * ORB_RADIUS/1.5D;
				double dz = (Math.random()-0.5D) * ORB_RADIUS/1.5D;
				level.addParticle(ParticleTypes.SOUL, x+dx, y+dy, z+dz, 0.0D, 0.0D, 0.0D);
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

		level.addParticle(new DelayedActionData(EnchantedParticleTypes.REMOVE_CURSE.get(), x, y, z, AbstractRemoveCurseRite.RAISE - age + Enchanted.RANDOM.nextInt(11)), pos.x, pos.y, pos.z, 0.0D, 0.0D, 0.0D);
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new RemoveCurseSeedParticle(level, x, y, z);
		}
	}

}
