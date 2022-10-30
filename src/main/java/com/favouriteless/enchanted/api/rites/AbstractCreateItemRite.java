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

package com.favouriteless.enchanted.api.rites;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Simple AbstractRite implementation for creating a single item
 */
public abstract class AbstractCreateItemRite extends AbstractRite {

    private final SoundEvent createItemSound;

    public AbstractCreateItemRite(int power, int powerTick, SoundEvent createItemSound) {
        super(power, powerTick);
        this.createItemSound = createItemSound;
    }

    protected void spawnItems(ItemStack... items) {
        if(level != null && !level.isClientSide) {
            for(ItemStack stack : items) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
                level.addFreshEntity(itemEntity);
            }
            level.playSound(null, pos, createItemSound, SoundSource.MASTER, 0.5F, 1.0F);

            spawnParticles();
        }
    }
}
