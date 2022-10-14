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

package com.favouriteless.enchanted.common.rites;

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class RiteOfSummoningEntity extends AbstractRite {

    public RiteOfSummoningEntity() {
        super(3000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_PURPLE.get());
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.TAGLOCK_FILLED.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.IRON_AXE, 1);
    }

    @Override
    public void execute() {
        if(targetEntity == null) targetEntity = getTargetEntity();
        if(world != null && pos != null && targetEntity != null) {
            spawnParticles(world, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
            spawnParticles((ServerWorld)targetEntity.level, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
            world.playSound(null, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENDERMAN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);
            targetEntity.level.playSound(null, targetEntity.getX(), targetEntity.getX(), targetEntity.getY(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.MASTER, 1.0F, 1.0F);

            if(world != targetEntity.level) {
                targetEntity.changeDimension(world);
            }
            targetEntity.teleportTo(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
        }
        else {
            cancel();
        }
        stopExecuting();
    }

    protected void spawnParticles(ServerWorld world, double x, double y, double z) {
        for(int i = 0; i < 25; i++) {
            double dx = x - 0.5D + (Math.random() * 1.5D);
            double dy = y + (Math.random() * 2.0D);
            double dz = z - 0.5D + (Math.random() * 1.5D);
            world.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onTick() {
    }

    @Override
    public RiteType<?> getType() {
        return EnchantedRiteTypes.SUMMONING_ENTITY.get();
    }

}
