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

import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.entity.RiteImprisonment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class ImprisonmentCageSeedParticle extends NoRenderParticle {

	protected ImprisonmentCageSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a++) {
			double angle = Math.toRadians(a);
			double cx = x + Math.sin(angle)*(RiteImprisonment.TETHER_RANGE + 0.3D);
			double cy = y;
			double cz = z + Math.cos(angle)*(RiteImprisonment.TETHER_RANGE + 0.3D);

			level.addParticle(EnchantedParticles.IMPRISONMENT_CAGE.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
			level.addParticle(EnchantedParticles.IMPRISONMENT_CAGE.get(), cx, cy + 4.0D, cz, 0.0D, 0.0D, 0.0D);

			if(a % 20 == 0) {
				for(int i = 0; i < 40; i++) {
					cy += 4.0D / 40;
					level.addParticle(EnchantedParticles.IMPRISONMENT_CAGE.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
				}
			}
		}
		this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
			return new ImprisonmentCageSeedParticle(level, pX, pY, pZ);
		}
	}
}
