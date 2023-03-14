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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

public class RepellingParticle extends TextureSheetParticle {

	private static final double REPEL_SPEED = 0.3D;

	private final double centerX;
	private final double centerY;
	private final double centerZ;

	protected RepellingParticle(ClientLevel level, double x, double y, double z, int red, int green, int blue, double centerX, double centerY, double centerZ) {
		super(level, x, y, z);
		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;
		this.alpha = 0.0F;
		this.lifetime = 60;
		this.hasPhysics = false;
		this.quadSize = 0.1F;
		this.rCol = red/255F;
		this.gCol = green/255F;
		this.bCol = blue/255F;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;

		if(age < lifetime) {
			if(alpha < 1.0F) {
				alpha += 0.2F;
				if(alpha > 1.0F)
					alpha = 1.0F;
			}
		}
		else {
			if(alpha > 0.0F) {
				alpha -= 0.02F;
				if(alpha < 0.0F) {
					alpha = 0.0F;
					remove();
				}
			}
		}
		Vec3 velocity = new Vec3(x - centerX, y - centerY, z - centerZ).normalize().scale(REPEL_SPEED);
		this.xd = velocity.x();
		this.yd = velocity.y();
		this.zd = velocity.z();

		this.move(xd, yd, zd);
		age++;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	public static class Factory implements ParticleProvider<CircleMagicData> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprites) {
			this.sprite = sprites;
		}

		public Particle createParticle(CircleMagicData data, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			RepellingParticle particle = new RepellingParticle(level, x, y, z, data.getRed(), data.getGreen(), data.getBlue(), data.getCenterX(), data.getCenterY(), data.getCenterZ());
			particle.pickSprite(this.sprite);
			return particle;
		}
	}
}
