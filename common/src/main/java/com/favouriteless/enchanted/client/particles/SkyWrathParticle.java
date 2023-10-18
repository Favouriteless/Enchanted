package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.types.DelayedActionParticleType.DelayedActionData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class SkyWrathParticle extends TextureSheetParticle {

	private static final double EXPLODE_SPEED = 0.7D;
	private static final double ATTRACT_SPEED = 0.02D;
	private static final double ORBIT_SPEED = 20.0D;

	private final double xCenter;
	private final double yCenter;
	private final double zCenter;
	private final int explodeTicks;

	protected SkyWrathParticle(ClientLevel level, double x, double y, double z, double xCenter, double yCenter, double zCenter, int explodeTicks) {
		super(level, x, y, z);
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		this.zCenter = zCenter;
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
			Vec3 relativePos = new Vec3(x - xCenter, y - yCenter, z - zCenter);
			double radius = relativePos.length() - ATTRACT_SPEED;
			double a = Math.toRadians(ORBIT_SPEED);
			Vec3 rotatedRelativePos = new Vec3(
					relativePos.x*Math.cos(a) - relativePos.z*Math.sin(a),
					relativePos.y,
					relativePos.z*Math.sin(a) + relativePos.x*Math.cos(a)
			);
			Vec3 newPos = rotatedRelativePos.normalize().scale(radius).add(new Vec3(xCenter, yCenter, zCenter));
			x = newPos.x();
			y = newPos.y();
			z = newPos.z();
		}
		else {
			Vec3 velocity = new Vec3(x - xCenter, y - yCenter, z - zCenter).normalize().scale(EXPLODE_SPEED);
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
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleProvider<DelayedActionData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(DelayedActionData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SkyWrathParticle particle = new SkyWrathParticle(level, x, y, z, data.getCenterX(), data.getCenterY(), data.getCenterZ(), data.getActionTicks());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}

}
