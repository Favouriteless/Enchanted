package net.favouriteless.enchanted.client.particles;

import net.favouriteless.enchanted.client.EParticleRenderTypes;
import net.favouriteless.enchanted.client.particles.types.ColouredCircleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class RepellingParticle extends TextureSheetParticle {

	private static final double REPEL_SPEED = 0.3D;

	private final Vec3 center;

	protected RepellingParticle(ClientLevel level, double x, double y, double z, float red, float green, float blue,
								Vec3 center) {
		super(level, x, y, z);
		this.center = center;
		this.alpha = 0.0F;
		this.lifetime = 60;
		this.hasPhysics = false;
		this.quadSize = 0.1F;
		this.rCol = red;
		this.gCol = green;
		this.bCol = blue;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;

		if(age < lifetime) {
			if(alpha < 1.0F) {
				alpha += 0.2F;
				if(alpha > 1.0F)
					alpha = 1.0F;
			}
		}
		else {
			if(alpha > 0.0F) {
				alpha -= 0.02F;
				if(alpha < 0.0F) {
					alpha = 0.0F;
					remove();
				}
			}
		}
		Vec3 velocity = new Vec3(x, y, z).subtract(center).normalize().scale(REPEL_SPEED);
		xd = velocity.x();
		yd = velocity.y();
		zd = velocity.z();

		move(xd, yd, zd);
		age++;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return EParticleRenderTypes.translucentParticle();
	}

	public static class Factory implements ParticleProvider<ColouredCircleOptions> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(ColouredCircleOptions data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RepellingParticle particle = new RepellingParticle(level, x, y, z, data.getRed(), data.getGreen(),
					data.getBlue(), data.center());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}

}
