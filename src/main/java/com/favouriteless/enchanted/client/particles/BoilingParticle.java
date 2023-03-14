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
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

import javax.annotation.Nullable;

public class BoilingParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected BoilingParticle(ClientLevel world, double x, double y, double z, int red, int green, int blue, SpriteSet pSprites) {
        super(world, x, y, z);
        this.rCol = red/255F;
        this.gCol = green/255F;
        this.bCol = blue/255F;
        this.sprites = pSprites;
        this.scale(Enchanted.RANDOM.nextFloat() * 0.4F);
        this.lifetime = Enchanted.RANDOM.nextInt(10) + 5;
        this.setSpriteFromAge(pSprites);
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        if (age++ >= lifetime) {
            remove();
        }
        else {
            setSpriteFromAge(sprites);
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
            return new BoilingParticle(world, x, y, z, data.getRed(), data.getGreen(), data.getBlue(), sprites);
        }
    }
}
