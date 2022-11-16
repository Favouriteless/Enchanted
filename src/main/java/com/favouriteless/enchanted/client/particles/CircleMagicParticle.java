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

import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CircleMagicParticle extends TextureSheetParticle {

    public static final double ANGLE = 2.0D;
    private final double radius;

    private final double xStart;
    private final double zStart;

    protected CircleMagicParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int red, int green, int blue, double xStart, double zStart, double radius) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = red/255F;
        this.gCol = green/255F;
        this.bCol = blue/255F;
        this.radius = radius;

        this.quadSize = 0.06F;
        this.age = 0;
        this.lifetime = 60;
        this.hasPhysics = false;

        this.xStart = xStart;
        this.zStart = zStart;

        // Create initial velocity
        this.xd = 0;
        this.yd = 0.04D;
        this.zd = 0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if(age++ >= lifetime) {
            this.alpha -= 0.01F;
            if(this.alpha <= 0) {
                remove();
            }
        }

        double angle = Math.toRadians(ANGLE);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        Vec3 newPos = new Vec3(
                cos * (x - xStart) - sin * (z - zStart),
                0,
                sin * (x - xStart) + cos * (z - zStart)
        ).normalize().scale(radius).add(xStart, 0, zStart);

        this.xd = newPos.x - this.x;
        this.zd = newPos.z - this.z;

        this.move(xd, yd, zd);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<CircleMagicData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(CircleMagicData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CircleMagicParticle particle = new CircleMagicParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue(), data.getCenterX(), data.getCenterZ(), data.getRadius());
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
