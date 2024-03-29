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
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class CurseSeedParticle extends NoRenderParticle {

	private static final double RADIUS = 1.5D;
	private static final double Y_INCREMENT = 0.2D;
	private double yOffset = 0.0D;

	protected CurseSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.lifetime = 20;
	}

	@Override
	public void tick() {
		if(age++ < lifetime) {
			double angle = Math.toRadians(age * 20);
			for(int i = 0; i < 20; i += 5) {
				double cx = x + Math.sin(angle+i) * RADIUS;
				double cy = y + Math.random() + yOffset;
				double cz = z + Math.cos(angle+i) * RADIUS;

				level.addParticle(ParticleTypes.SOUL, cx, cy, cz, 0.0D, 0.0D, 0.0D);
			}
			yOffset += Y_INCREMENT;
		}
		else
			this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CurseSeedParticle(level, x, y, z);
		}
	}
}
