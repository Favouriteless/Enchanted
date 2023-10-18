package com.favouriteless.enchanted.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ImprisonmentCageParticle extends TextureSheetParticle {

	public static final int LIFETIME = 40;

	protected ImprisonmentCageParticle(ClientLevel level, double x, double y, double z) {
		super(level, x, y, z);
		this.lifetime = LIFETIME;
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
			alpha -= 0.02F;
			if(alpha <= 0)
				remove();
		}
		else if(alpha < 1.0F) {
			alpha += 0.04F;
			if(alpha > 1.0F)
				alpha = 1.0F;
		}
	}

	@Override
	public int getLightColor(float partialTick) {
		float f = ((float)age + partialTick) / (float)lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(partialTick);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int)(f * 15.0F * 16.0F);
		if (j > 240) {
			j = 240;
		}

		return j | k << 16;
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
			ImprisonmentCageParticle particle = new ImprisonmentCageParticle(level, x, y, z);
			particle.pickSprite(this.sprite);
			particle.scale(0.5F);
			return particle;
		}
	}
}
