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

import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class RiteSummonFamiliar extends AbstractRite {

    public RiteSummonFamiliar() {
        super(RiteTypes.SUMMONING_FAMILIAR.get(), 1000, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.MEDIUM, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(EnchantedItems.BREATH_OF_THE_GODDESS.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.HINT_OF_REBIRTH.get(), 1);
        ITEMS_REQUIRED.put(EnchantedItems.WHIFF_OF_MAGIC.get(), 1);
    }

    @Override
    public void execute() {
        IFamiliarEntry entry = level.getServer().getLevel(Level.OVERWORLD).getCapability(EnchantedCapabilities.FAMILIAR).orElse(null).getFamiliarFor(casterUUID);

        if(entry != null) {
            Vec3 vec3 = Vec3.atCenterOf(pos);
            targetEntity = checkForExisting(entry.getUUID());
            if(targetEntity == null) {
                targetEntity = entry.getType().getTypeOut().create(level);
                targetEntity.load(entry.getNbt());
                targetEntity.setPos(vec3);
                level.addFreshEntity(targetEntity);
                ((TamableAnimal)targetEntity).setPersistenceRequired();

                entry.setUUID(targetEntity.getUUID()); // Update the UUID entry
            }

            if(targetEntity.getLevel() != level)
                targetEntity.changeDimension(level, new SummonForcer(vec3));
            else
                targetEntity.teleportTo(vec3.x, vec3.y, vec3.z);

            entry.setDismissed(false);
            level.playSound(null, vec3.x, vec3.y, vec3.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.0F, 1.0F);
            teleportParticles(level, vec3.x, vec3.y, vec3.z);
        }
        else {
            cancel();
        }
        stopExecuting();
    }

    @Override
    protected boolean checkAdditional() {
        return true;
    }

    protected Entity checkForExisting(UUID uuid) {
        for(ServerLevel _level : level.getServer().getAllLevels()) {
            Entity entity = _level.getEntity(uuid);
            if(entity != null)
                return entity;
        }
        return null;
    }

    protected void teleportParticles(ServerLevel world, double x, double y, double z) {
        for(int i = 0; i < 25; i++) {
            double dx = x - 0.5D + (Math.random() * 1.5D);
            double dy = y + (Math.random() * 2.0D);
            double dz = z - 0.5D + (Math.random() * 1.5D);
            world.sendParticles(ParticleTypes.PORTAL, dx, dy, dz, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

}
