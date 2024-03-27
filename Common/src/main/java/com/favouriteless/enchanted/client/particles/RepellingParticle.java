package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class RepellingParticle extends TextureSheetParticle {

	private static final double REPEL_SPEED = 0.3D;

	private final double xCenter;
	private final double yCenter;
	private final double zCenter;

	protected RepellingParticle(ClientLevel level, double x, double y, double z, int red, int green, int blue, double xCenter, double yCenter, double zCenter) {
		super(level, x, y, z);
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		this.zCenter = zCenter;
		this.alpha = 0.0F;
		this.lifetime = 60;
		this.hasPhysics = false;
		this.quadSize = 0.1F;
		this.rCol = red/255F;
		this.gCol = green/255F;
		this.bCol = blue/255F;
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
		Vec3 velocity = new Vec3(x - xCenter, y - yCenter, z - zCenter).normalize().scale(REPEL_SPEED);
		xd = velocity.x();
		yd = velocity.y();
		zd = velocity.z();

		move(xd, yd, zd);
		age++;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleProvider<CircleMagicData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(CircleMagicData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RepellingParticle particle = new RepellingParticle(level, x, y, z, data.getRed(), data.getGreen(), data.getBlue(), data.getCenterX(), data.getCenterY(), data.getCenterZ());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}

}
