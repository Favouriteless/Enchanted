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

package com.favouriteless.enchanted.common.rites.entity;

import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.rites.CirclePart;
import net.minecraft.world.item.Items;

public class RiteProtectionTemporary extends RiteProtection {

    public static final int DURATION = 60 * 20; // 60 seconds duration

    public RiteProtectionTemporary() {
        super(RiteTypes.PROTECTION_TEMPORARY.get(), 0, 0, 4, EnchantedBlocks.PROTECTION_BARRIER_TEMPORARY.get()); // Power, power per tick, radius
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.OBSIDIAN, 1);
        ITEMS_REQUIRED.put(EnchantedItems.ATTUNED_STONE_CHARGED.get(), 1);
    }

    @Override
    protected void onTick() {
        if(level.isLoaded(targetPos)) {
            if(ticks % 20 == 0) {
                generateSphere(block);
                level.sendParticles(new DoubleParticleData(EnchantedParticles.PROTECTION_SEED.get(), radius), targetPos.getX() + 0.5D, targetPos.getY() + 0.6D, targetPos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
        if(ticks > DURATION)
            stopExecuting();
    }

}
