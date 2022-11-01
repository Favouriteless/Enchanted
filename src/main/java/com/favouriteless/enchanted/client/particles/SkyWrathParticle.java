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

import com.favouriteless.enchanted.client.particles.SkyWrathParticleType.SkyWrathData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class SkyWrathParticle extends TextureSheetParticle {

	private static final double EXPLODE_SPEED = 0.7D;
	private static final double ATTRACT_SPEED = 0.02D;
	private static final double ORBIT_SPEED = 20.0D;

	private final double centerX;
	private final double centerY;
	private final double centerZ;
	private final int explodeTicks;

	protected SkyWrathParticle(ClientLevel level, double x, double y, double z, int red, int green, int blue, double centerX, double centerY, double centerZ, int explodeTicks) {
		super(level, x, y, z);
		this.rCol = red/255F;
		this.gCol = green/255F;
		this.bCol = blue/255F;
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.explodeTicks = explodeTicks;
		this.alpha = 0.0F;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if(age < explodeTicks) {
			if(alpha < 1.0F) {
				alpha += 0.3F;
				if(alpha > 1.0F)
					alpha = 1.0F;
			}
			Vec3 relativePos = new Vec3(x - centerX, y - centerY, z - centerZ);
			double radius = relativePos.length() - ATTRACT_SPEED;
			double a = Math.toRadians(ORBIT_SPEED);
			Vec3 rotatedRelativePos = new Vec3(
					relativePos.x*Math.cos(a) - relativePos.z*Math.sin(a),
					relativePos.y,
					relativePos.z*Math.sin(a) + relativePos.x*Math.cos(a)
			);
			Vec3 newPos = rotatedRelativePos.normalize().scale(radius).add(new Vec3(centerX, centerY, centerZ));
			this.x = newPos.x();
			this.y = newPos.y();
			this.z = newPos.z();
		}
		else {
			Vec3 velocity = new Vec3(x - centerX, y - centerY, z - centerZ).normalize().scale(EXPLODE_SPEED);
			this.xd = velocity.x();
			this.yd = velocity.y();
			this.zd = velocity.z();
			this.move(xd, yd, zd);
			this.alpha -= 0.06F;

			if(alpha < 0.0F)
				remove();
		}

		age++;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleProvider<SkyWrathData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(SkyWrathData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			SkyWrathParticle particle = new SkyWrathParticle(level, x, y, z, data.getRed(), data.getGreen(), data.getBlue(), data.getCenterX(), data.getCenterY(), data.getCenterZ(), data.getExplodeTicks());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
