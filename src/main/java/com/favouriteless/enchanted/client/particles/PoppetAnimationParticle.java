/*
 * Copyright (c) 2021. Favouriteless
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

import com.favouriteless.enchanted.client.particles.PoppetAnimationParticleType.PoppetAnimationData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;

import javax.annotation.Nullable;

public class PoppetAnimationParticle extends ScreenSpaceTexturedParticle {

    protected PoppetAnimationParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int red, int green, int blue) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = red / 255F;
        this.gCol = green / 255F;
        this.bCol = blue / 255F;
        this.gravity = 0f;
        this.lifetime = 3000;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.CUSTOM;
    }

    public static class Factory implements IParticleFactory<PoppetAnimationData> {

        private final IAnimatedSprite sprites;

        public Factory(IAnimatedSprite pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(PoppetAnimationData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PoppetAnimationParticle particle = new PoppetAnimationParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }

}
