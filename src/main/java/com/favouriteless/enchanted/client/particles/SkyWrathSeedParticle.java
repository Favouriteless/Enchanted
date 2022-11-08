/*
 *
 *   Copyright (c) 2022. Favouriteless
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

import com.favouriteless.enchanted.client.particles.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.rites.RiteOfSkyWrath;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SkyWrathSeedParticle extends NoRenderParticle {

	private static final double xSpread = 0.3D;
	private static final double ySpread = 0.3D;
	private static final double zSpread = 0.3D;

	protected SkyWrathSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.lifetime = 60;
		this.hasPhysics = false;
	}

	@Override
	public void tick() {
		if(age++ < lifetime) {
			if(age % 3 == 0) {
				for(int a = 0; a < 360; a += 20) {
					double angle = Math.toRadians(a);

					double cx = x + Math.sin(angle) * RiteOfSkyWrath.LIGHTNING_RADIUS + Math.random() * xSpread;
					double cy = y + Math.random() * ySpread;
					double cz = z + Math.cos(angle) * RiteOfSkyWrath.LIGHTNING_RADIUS + Math.random() * zSpread;

					level.addParticle(new DelayedActionData(EnchantedParticles.SKY_WRATH.get(), x, y, z, RiteOfSkyWrath.EXPLODE-age), cx, cy, cz, 0, 0, 0);
				}
			}
		}
		else
			this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new SkyWrathSeedParticle(level, x, y, z);
		}
	}
}
