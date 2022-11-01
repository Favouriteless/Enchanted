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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.Items;

public class RiteOfSkyWrath extends AbstractRite {

    public static final int START_RAINING = 120;
    public static final int EXPLODE = 160;
    public static final double LIGHTNING_RADIUS = 5;

    public RiteOfSkyWrath(int power, int powerTick) {
        super(power, powerTick);
    }

    public RiteOfSkyWrath() {
        this(1000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.WOODEN_SWORD, 1);
        ITEMS_REQUIRED.put(EnchantedItems.WOOD_ASH.get(), 1);
    }

    @Override
    public void execute() {
        detatchFromChalk();
        level.sendParticles(EnchantedParticles.SKY_WRATH_SEED.get(), pos.getX()+0.5D, pos.getY()+2.0D, pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        ticks = 0;
    }

    @Override
    public void onTick() {
        if(ticks == START_RAINING) {
            level.setWeatherParameters(0, 6000, true, true);
        }
        else if(ticks > EXPLODE) {
            spawnLightning(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
            stopExecuting();
        }
    }

    protected void spawnLightning(double x, double y, double z) {
        for(int a = 0; a < 360; a+=60) {
            double angle = Math.toRadians(a);
            double cx = x + Math.sin(angle) * LIGHTNING_RADIUS;
            double cz = z + Math.cos(angle) * LIGHTNING_RADIUS;

            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
            lightningBolt.moveTo(cx, y, cz);
            level.addFreshEntity(lightningBolt);
        }
    }

    @Override
    public RiteType<?> getType() {
        return EnchantedRiteTypes.SKY_WRATH.get();
    }

}
