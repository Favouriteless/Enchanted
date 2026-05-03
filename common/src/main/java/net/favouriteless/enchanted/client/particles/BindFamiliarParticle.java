package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.init.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.DelayedPosOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class BindFamiliarParticle extends TextureSheetParticle {

	private static final double EXPLODE_SPEED = 0.6D;
	private static final double ATTRACT_SPEED = 0.06D;

	private final SpriteSet sprites;
	private final int fallTicks;
	private final Vec3 center;

	protected BindFamiliarParticle(ClientLevel level, double x, double y, double z, Vec3 center, int fallTicks, SpriteSet sprites) {
		super(level, x, y, z);
		this.sprites = sprites;
		this.center = center;
		this.fallTicks = fallTicks;
		this.alpha = 0.0F;

		this.quadSize = 0.125F * (this.random.nextFloat() * 0.2F + 0.5F);
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.rCol = f * 0.9F;
		this.gCol = f * 0.3F;
		this.bCol = f;

		this.hasPhysics = false;
		this.lifetime = 100;
		this.friction = 0.85F;
		this.gravity = 0.1F;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		Vec3 relativePos = new Vec3(x, y, z).subtract(center);
		if(age++ < fallTicks) {
			if(alpha < 1.0F) {
				alpha += 0.05F;
				if(alpha > 1.0F)
					alpha = 1.0F;
			}

			if(relativePos.length() <= BindFamiliarSeedParticle.ORB_RADIUS) {
				xd = 0.0D;
				yd = 0.0D;
				zd = 0.0D;
			}
			else {
				Vec3 velocity = relativePos.normalize().scale(-1 * ATTRACT_SPEED);
				xd = velocity.x();
				yd = velocity.y();
				zd = velocity.z();
			}
		}
		else {
			if(age == fallTicks + 1) {
				Vec3 velocity = relativePos.normalize().scale(EXPLODE_SPEED);
				xd = velocity.x();
				yd = velocity.y();
				zd = velocity.z();
				this.hasPhysics = true;
			}

			if(alpha > 0.0F) {
				alpha -= 0.05F;
				this.xd *= friction;
				this.yd -= gravity;
				this.zd *= friction;
			}
			else
				remove();
		}
		this.setSpriteFromAgeWrapped();
		this.move(xd, yd, zd);
	}

	private void setSpriteFromAgeWrapped() {
		this.setSprite(sprites.get(age % lifetime, lifetime));
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
			BindFamiliarParticle particle = new BindFamiliarParticle(level, x, y, z, data.center(), data.delay(), sprite);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
