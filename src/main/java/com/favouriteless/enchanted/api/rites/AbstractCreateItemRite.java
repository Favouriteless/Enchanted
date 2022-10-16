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

package com.favouriteless.enchanted.api.rites;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;

/**
 * Simple AbstractRite implementation for creating a single item
 */
public abstract class AbstractCreateItemRite extends AbstractRite {

    public AbstractCreateItemRite(int power, int powerTick) {
        super(power, powerTick);
    }

    protected void spawnItems(ItemStack... item) {
        if(world != null && !world.isClientSide) {
            for(ItemStack stack : item) {
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                world.addFreshEntity(itemEntity);
            }
            world.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);

            spawnParticles();
        }
    }
}
