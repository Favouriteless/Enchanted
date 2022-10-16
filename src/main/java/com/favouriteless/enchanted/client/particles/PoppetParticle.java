/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.client.particles;

import com.favouriteless.enchanted.client.particles.TwoToneColouredParticleType.TwoToneColouredData;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;

import javax.annotation.Nullable;

public class PoppetParticle extends SimpleAnimatedParticle {

    protected PoppetParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet pSprites, int red, int green, int blue, int red1, int green1, int blue1) {
        super(world, x, y, z, pSprites, -0.05F);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize *= 0.75F;
        this.lifetime = 60 + this.random.nextInt(12);
        this.setSpriteFromAge(pSprites);

        if (this.random.nextInt(4) == 0) {
            this.setColor(red1/255F, green1/255F, blue1/255F);
        } else {
            this.setColor(red/255F, green/255F, blue/255F);
        }

        this.setBaseAirFriction(0.6F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<TwoToneColouredData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(TwoToneColouredData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PoppetParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, sprites, data.getRed(), data.getGreen(), data.getBlue(), data.getRed1(), data.getGreen1(), data.getBlue1());
        }
    }
}
