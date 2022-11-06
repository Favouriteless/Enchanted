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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.rites.AbstractRemoveCurseRite;
import com.favouriteless.enchanted.client.particles.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;

public class RemoveCurseSeedParticle extends NoRenderParticle {

	private static final double RADIUS = 4.5D;
	public static final double ORB_RADIUS = 1.0D;

	protected RemoveCurseSeedParticle(ClientLevel pLevel, double x, double y, double z) {
		super(pLevel, x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.lifetime = 200;
	}

	@Override
	public void tick() {
		if(age++ < lifetime) {
			spawnParticle();
			if(Math.random() < 0.5D)
				spawnParticle();
		}
		else
			remove();
	}

	private void spawnParticle() {
		double u = Math.random();
		double x1 = Enchanted.RANDOM.nextGaussian();
		double x2 = Enchanted.RANDOM.nextGaussian();
		double x3 = Enchanted.RANDOM.nextGaussian();
		double mag = Math.sqrt(x1 * x1 + x2 * x2 + x3 * x3);
		x1 /= mag;
		x2 /= mag;
		x3 /= mag;
		double c = Math.cbrt(u);
		Vec3 spherePos = new Vec3(x1 * c, x2 * c, x3 * c).scale(RADIUS);

		level.addParticle(new DelayedActionData(EnchantedParticles.REMOVE_CURSE.get(), x, y, z, AbstractRemoveCurseRite.RAISE - age + (int)Math.round(Math.random()*10)), x + spherePos.x, y + spherePos.y, z + spherePos.z, 0.0D, 0.0D, 0.0D);
	}

	public static class Factory implements ParticleProvider<SimpleParticleType> {

		public Factory(SpriteSet sprites) {
		}

		public Particle createParticle(SimpleParticleType data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new RemoveCurseSeedParticle(level, x, y, z);
		}
	}
}
