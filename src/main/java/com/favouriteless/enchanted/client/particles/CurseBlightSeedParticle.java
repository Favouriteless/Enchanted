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

import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class CurseBlightSeedParticle extends NoRenderParticle {

	protected CurseBlightSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a+=2) {
			double angle = Math.toRadians(a);
			double cx = x + Math.sin(angle) * 0.1D;
			double cz = z + Math.cos(angle) * 0.1D;

			level.addParticle(new CircleMagicData(EnchantedParticles.CURSE_BLIGHT.get(), 31, 30, 77, x, y, z, 0.1D), cx, y, cz, 0.0D, 0.0D, 0.0D);
		}
		this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CurseBlightSeedParticle(level, x, y, z);
		}
	}
}
