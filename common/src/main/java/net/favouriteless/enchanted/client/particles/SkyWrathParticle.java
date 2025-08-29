package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.DelayedPosOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class SkyWrathParticle extends TextureSheetParticle {

	private static final double EXPLODE_SPEED = 0.7D;
	private static final double ATTRACT_SPEED = 0.02D;
	private static final double ORBIT_SPEED = 20.0D;

	private final Vec3 center;
	private final int explodeTicks;

	protected SkyWrathParticle(ClientLevel level, double x, double y, double z, Vec3 center, int explodeTicks) {
		super(level, x, y, z);
		this.center = center;
		this.explodeTicks = explodeTicks;
		this.alpha = 0.0F;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;

		if(age < explodeTicks) {
			if(alpha < 1.0F) {
				alpha += 0.3F;
				if(alpha > 1.0F)
					alpha = 1.0F;
			}
			Vec3 relativePos = new Vec3(x , y, z).subtract(center);
			double radius = relativePos.length() - ATTRACT_SPEED;
			double a = Math.toRadians(ORBIT_SPEED);
			Vec3 rotatedRelativePos = new Vec3(
					relativePos.x*Math.cos(a) - relativePos.z*Math.sin(a),
					relativePos.y,
					relativePos.z*Math.sin(a) + relativePos.x*Math.cos(a)
			);
			Vec3 newPos = rotatedRelativePos.normalize().scale(radius).add(center);
			x = newPos.x();
			y = newPos.y();
			z = newPos.z();
		}
		else {
			Vec3 velocity = new Vec3(x, y, z).subtract(center).normalize().scale(EXPLODE_SPEED);
			xd = velocity.x();
			yd = velocity.y();
			zd = velocity.z();
			move(xd, yd, zd);
			alpha -= 0.06F;

			if(alpha < 0.0F)
				remove();
		}

		age++;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return EParticleRenderTypes.translucentParticle();
	}

	public static class Factory implements ParticleProvider<DelayedPosOptions> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(DelayedPosOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SkyWrathParticle particle = new SkyWrathParticle(level, x, y, z, data.center(), data.delay());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}

}
