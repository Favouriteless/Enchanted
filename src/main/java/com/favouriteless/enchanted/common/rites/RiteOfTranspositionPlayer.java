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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class RiteOfTranspositionPlayer extends AbstractRite {

    public RiteOfTranspositionPlayer() {
        super(0, 0); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_PURPLE.get());
        ITEMS_REQUIRED.put(EnchantedItems.BOUND_WAYSTONE.get(), 1);
    }

    @Override
    public void execute() {
        ItemStack waystone = itemsConsumed.get(0);
        Player caster = world.getPlayerByUUID(casterUUID);

        if(caster != null && waystone.hasTag()) {
            CompoundTag nbt = waystone.getTag();
            ServerLevel targetWorld;
            double x;
            double y;
            double z;

            if(nbt.contains("uuid")) {
                targetUUID = nbt.getUUID("uuid");
                targetEntity = getTargetEntity();

                if(targetEntity == null) {
                    cancel();
                    return;
                }

                targetWorld = (ServerLevel)targetEntity.level;
                x = targetEntity.getX();
                y = targetEntity.getY();
                z = targetEntity.getZ();
            }
            else {
                ResourceKey<Level> dimensionKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(nbt.getString("dimension")));
                targetWorld = caster.getServer().getLevel(dimensionKey);
                x = nbt.getInt("xPos");
                y = nbt.getInt("yPos");
                z = nbt.getInt("zPos");
            }

            if(targetWorld != null) {
                spawnParticles((ServerLevel) caster.level, caster.getX(), caster.getY(), caster.getZ());
                world.playSound(null, caster.position().x, caster.position().y, caster.position().z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
                if(caster.level != targetWorld) {
                    caster.changeDimension(targetWorld);
                }
                caster.teleportTo(x + 0.5D, y + 0.5D, z + 0.5D);
                world.playSound(null, caster.position().x, caster.position().y, caster.position().z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
                spawnParticles(targetWorld, x + 0.5D, y, z + 0.5D);
            }
            else {
                cancel();
            }
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

    @Override
    public void onTick() {
    }

    @Override
    public RiteType<?> getType() {
        return EnchantedRiteTypes.TRANSPOSITION_PLAYER.get();
    }

}
