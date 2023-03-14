/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ImprisonmentCageParticle extends TextureSheetParticle {

	public static final int LIFETIME = 40;

	protected ImprisonmentCageParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
		super(pLevel, pX, pY, pZ);
		this.lifetime = LIFETIME;
		this.quadSize = 0.1F;
		this.alpha = 0.0F;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(age++ >= lifetime) {
			alpha -= 0.02F;
			if(alpha <= 0) {
				remove();
			}
		}
		else if(alpha < 1.0F) {
			alpha += 0.04F;
			if(alpha > 1.0F)
				alpha = 1.0F;
		}
	}

	@Override
	public int getLightColor(float pPartialTick) {
		float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
		f = Mth.clamp(f, 0.0F, 1.0F);
		int i = super.getLightColor(pPartialTick);
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

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
			ImprisonmentCageParticle particle = new ImprisonmentCageParticle(level, pX, pY, pZ);
			particle.pickSprite(this.sprite);
			particle.scale(0.5F);
			return particle;
		}
	}
}
