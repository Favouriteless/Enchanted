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
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType.SimpleColouredData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CauldronCookParticle extends TextureSheetParticle {

    public static final double ANGLE = 8.0D;
    public static final double RADIUS_INCREASE = 0.005D;

    private final int circleStart;
    private final double xStart;
    private final double zStart;

    private double currentRadius;

    protected CauldronCookParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int red, int green, int blue) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.rCol = Math.min((red + Enchanted.RANDOM.nextInt(40) - 20)/255F, 1.0F);
        this.gCol = Math.min((green + Enchanted.RANDOM.nextInt(40) - 20)/255F, 1.0F);
        this.bCol = Math.min((blue + Enchanted.RANDOM.nextInt(40) - 20)/255F, 1.0F);

        this.scale(random.nextFloat() * 0.6F);
        this.age = 0;
        this.lifetime = 80;
        this.hasPhysics = false;
        this.circleStart = Enchanted.RANDOM.nextInt(5) + 10;

        this.xStart = x;
        this.zStart = z;
        // Create random initial velocity
        this.xd = (Math.random() * 0.005D) - 0.0025D;
        this.yd = 0.04D;
        this.zd = (Math.random() * 0.005D) - 0.0025D;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (age++ >= lifetime) {
            this.alpha -= 0.1F;
            if(this.alpha <= 0) {
                remove();
            }
        }

        if(age >= circleStart) {
            if(age == circleStart) { // Just started rotating
                this.xd = 0;
                this.yd = 0;
                this.zd = 0;
                double xOffset = x - xStart;
                double zOffset = z - zStart;
                this.currentRadius = Math.sqrt((xOffset * xOffset) + (zOffset * zOffset));
            }

            currentRadius += RADIUS_INCREASE;
            double angle = Math.toRadians(ANGLE);
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);

            Vec3 newPos = new Vec3(
                    cos * (x - xStart) - sin * (z - zStart),
                    0,
                    sin * (x - xStart) + cos * (z - zStart)
            ).normalize().scale(currentRadius).add(xStart, 0.0D, zStart);

            this.x = newPos.x;
            this.z = newPos.z;
        }
        this.move(xd, yd, zd);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleColouredData> {

        private final SpriteSet sprites;

        public Factory(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleColouredData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            CauldronCookParticle particle = new CauldronCookParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.getRed(), data.getGreen(), data.getBlue());
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
