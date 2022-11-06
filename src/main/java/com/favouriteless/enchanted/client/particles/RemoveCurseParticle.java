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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class RemoveCurseParticle extends TextureSheetParticle {

	private static final double RAISE_SPEED = 0.7D;
	private static final double RAISE_ACCELERATION = 0.05D;
	private static final double ATTRACT_SPEED = 0.06D;
	private final int raiseTicks;
	private final double centerX;
	private final double centerY;
	private final double centerZ;

	protected RemoveCurseParticle(ClientLevel level, double x, double y, double z, double centerX, double centerY, double centerZ, int raiseTicks) {
		super(level, x, y, z);
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.raiseTicks = raiseTicks;
		this.alpha = 0.0F;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if(age++ < raiseTicks) {
			if(alpha < 1.0F) {
				alpha += 0.05F;
				if(alpha > 1.0F)
					alpha = 1.0F;
			}

			Vec3 relativePos = new Vec3(x, y, z).subtract(centerX, centerY, centerZ);
			if(relativePos.length() <= RemoveCurseSeedParticle.ORB_RADIUS) {
				xd = 0.0D;
				yd = 0.0D;
				zd = 0.0D;
			}
			else {
				Vec3 velocity = relativePos.normalize().scale(-1 * ATTRACT_SPEED);
				xd = velocity.x();
				yd = velocity.y();
				zd = velocity.z();
			}
		}
		else {
			if(alpha > 0.0F) {
				alpha -= 0.05F;
				xd = 0.0D;
				yd = Math.min(yd + RAISE_ACCELERATION, RAISE_SPEED);
				zd = 0.0D;
			}
			else
				remove();
		}
		this.move(xd, yd, zd);
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleProvider<DelayedActionData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(DelayedActionData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RemoveCurseParticle particle = new RemoveCurseParticle(level, x, y, z, data.getCenterX(), data.getCenterY(), data.getCenterZ(), data.getActionTicks());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
