package com.favouriteless.enchanted.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class ProtectionParticle extends TextureSheetParticle {

	protected ProtectionParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
		this.lifetime = 30;
		this.quadSize = 0.1F;
		this.alpha = 0.0F;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		xo = x;
		yo = y;
		zo = z;
		if(age++ >= lifetime) {
			alpha -= 0.04F;
			if(alpha <= 0.0F)
				remove();
		}
		else if(alpha < 1.0F) {
			alpha += 0.04F;
			if(alpha > 1.0F)
				alpha = 1.0F;
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			ProtectionParticle particle = new ProtectionParticle(level, x, y, z);
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
