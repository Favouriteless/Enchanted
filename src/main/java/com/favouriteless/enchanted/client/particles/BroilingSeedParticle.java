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

import com.favouriteless.enchanted.common.rites.processing.RiteBroiling;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class BroilingSeedParticle extends NoRenderParticle {

	protected BroilingSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a+=6) {
			double angle = Math.toRadians(a);
			double cx = xo + Math.sin(angle) * RiteBroiling.CIRCLE_RADIUS;
			double cz = zo + Math.cos(angle) * RiteBroiling.CIRCLE_RADIUS;

			level.addParticle(ParticleTypes.FLAME, cx, yo, cz, 0.0D, 0.0D, 0.0D);
		}
		this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet pSprites) {
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new BroilingSeedParticle(world, x, y, z);
		}
	}
}
