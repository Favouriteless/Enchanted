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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class FertilitySeedParticle extends NoRenderParticle {

	protected FertilitySeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void tick() {
		for(int a = 0; a < 360; a+=2) {
			double cx = Enchanted.RANDOM.nextGaussian();
			double cy = Enchanted.RANDOM.nextGaussian();
			double cz = Enchanted.RANDOM.nextGaussian();;
			Vec3 pos = new Vec3(cx, cy, cz).normalize().scale(0.1D).add(x, y, z);

			level.addParticle(new CircleMagicData(EnchantedParticles.FERTILITY.get(), 255, 255, 255, x, y, z, 0.1D), pos.x, pos.z, pos.y, 0.0D, 0.0D, 0.0D);
		}
		this.remove();
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new FertilitySeedParticle(level, x, y, z);
		}
	}
}
