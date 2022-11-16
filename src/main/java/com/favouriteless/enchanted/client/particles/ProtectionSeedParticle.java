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

import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class ProtectionSeedParticle extends NoRenderParticle {

	private final double radius;

	protected ProtectionSeedParticle(ClientLevel pLevel, double x, double y, double z, double radius) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.hasPhysics = false;
		this.radius = radius;
	}

	@Override
	public void tick() {
		double increment = 1.0D / radius; // Total radians / circumference for radians per step
		for(double y = 0; y <= Math.PI*2 + increment/2; y += increment) {
			for(double p = 0; p <= Math.PI*2 + increment/2; p += increment) {
				double cosY = Math.cos(y);
				double sinY = Math.sin(y);
				double cosP = Math.cos(p);
				double sinP = Math.sin(p);
				double cx = sinY * cosP * radius + this.x + (Math.random()-0.5D);
				double cy = sinP * radius + this.y + (Math.random()-0.5D);
				double cz = cosY * cosP * radius + this.z + (Math.random()-0.5D);

				if(Math.random() < 0.5D)
					level.addParticle(EnchantedParticles.PROTECTION.get(), cx, cy, cz, 0.0D, 0.0D, 0.0D);
			}
		}
		remove();
	}

	public static class Factory implements ParticleProvider<DoubleParticleData> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(DoubleParticleData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new ProtectionSeedParticle(level, x, y, z, data.getValue());
		}
	}
}
