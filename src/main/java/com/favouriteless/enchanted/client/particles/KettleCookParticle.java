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
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nullable;

public class KettleCookParticle extends TextureSheetParticle {

    protected KettleCookParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int red, int green, int blue) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min((red + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);
        this.gCol = Math.min((green + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);
        this.bCol = Math.min((blue + Enchanted.RANDOM.nextInt(20) - 10)/255F, 1.0F);

        this.scale(random.nextFloat() * 0.4F);
        this.lifetime = 20;
        this.gravity = 0.02F;
        this.hasPhysics = false;

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (lifetime-- <= 0) {
            this.alpha -= 0.1F;

            if(this.alpha <= 0) {
                remove();
            }
        } else {
            yd = -gravity;
            this.move(xd, yd, zd);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleColouredParticleType.SimpleColouredData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleColouredParticleType.SimpleColouredData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            KettleCookParticle particle = new KettleCookParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
