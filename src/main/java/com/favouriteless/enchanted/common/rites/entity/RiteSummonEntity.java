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

import com.favouriteless.enchanted.api.rites.AbstractRite;
import com.favouriteless.enchanted.common.init.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.RiteTypes;
import com.favouriteless.enchanted.common.rites.CirclePart;
import com.favouriteless.enchanted.common.rites.SummonForcer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class RiteSummonEntity extends AbstractRite {

    public RiteSummonEntity() {
        super(RiteTypes.SUMMONING_ENTITY.get(), 3000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_PURPLE.get());
        ITEMS_REQUIRED.put(EnchantedItems.WAYSTONE.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.TAGLOCK_FILLED.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.ENDER_DEW.get(), 1);
        ITEMS_REQUIRED.put(Items.IRON_AXE, 1);
    }

    @Override
    public void execute() {
        if(targetEntity == null)
            targetEntity = tryFindTargetEntity();
        if(level != null && pos != null && targetEntity != null) {
            spawnParticles(level, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
            spawnParticles((ServerLevel)targetEntity.level, targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
            level.playSound(null, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
            targetEntity.level.playSound(null, targetEntity.getX(), targetEntity.getX(), targetEntity.getY(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);

            Vec3 destination = new Vec3(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
            if(level != targetEntity.level)
                targetEntity.changeDimension(level, new SummonForcer(destination));
            else
                targetEntity.teleportTo(destination.x, destination.y, destination.z);
        }
        else {
            cancel();
        }
        stopExecuting();
    }

    protected void spawnParticles(ServerLevel world, double x, double y, double z) {
        for(int i = 0; i < 25; i++) {
            double dx = x - 0.5D + (Math.random() * 1.5D);
            double dy = y + (Math.random() * 2.0D);
            double dz = z - 0.5D + (Math.random() * 1.5D);
            world.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

}
