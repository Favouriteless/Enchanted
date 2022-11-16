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
import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.util.rite.CirclePart;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class RiteOfSanctity extends AbstractRite {

    public static final double REPULSE_FACTOR = 0.03D;

    public RiteOfSanctity() {
        super(EnchantedRiteTypes.SANCTITY.get(), 500, 3); // Power, power per tick
        CIRCLES_REQUIRED.put(CirclePart.SMALL, EnchantedBlocks.CHALK_WHITE.get());
        ITEMS_REQUIRED.put(Items.FEATHER, 1);
        ITEMS_REQUIRED.put(Items.REDSTONE, 1);
    }

    @Override
    public void execute() {
        level.playSound(null, pos, SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.MASTER, 0.5F, 1.0F);
    }

    @Override
    public void onTick() {
        List<Entity> currentEntities = CirclePart.SMALL.getEntitiesInside(level, pos, entity -> ForgeRegistries.ENTITIES.tags().getTag(EnchantedTags.MONSTERS).contains(entity.getType()));
        if(!currentEntities.isEmpty()) {
            for(Entity entity : currentEntities) {
                Vec3 opposingVector = entity.position().subtract(pos.getX(), entity.position().y(), pos.getZ());
                double distance = opposingVector.distanceTo(new Vec3(pos.getX()+0.5D, 0.0D, pos.getZ()+0.5D));
                entity.setDeltaMovement(entity.getDeltaMovement().add(opposingVector.normalize().scale(distance * 10).scale(REPULSE_FACTOR / 500D)));
            }
        }

        if(this.ticks % 2 == 0) {
            double cx = pos.getX() + 0.5D;
            double cz = pos.getZ() + 0.5D;
            double dy = pos.getY() + 0.1D;
            double dz = pos.getZ() + 0.5D;

            level.sendParticles(new CircleMagicData(EnchantedParticles.CIRCLE_MAGIC.get(), 255, 255, 255, cx, pos.getY(), cz, 3.0D), cx + 3.0D, dy, dz, 1, 0.0D, 0.35D, 0.0D, 0.0D);
            level.sendParticles(new CircleMagicData(EnchantedParticles.CIRCLE_MAGIC.get(), 255, 255, 255, cx, pos.getY(), cz, 3.0D), cx - 3.0D, dy, dz, 1, 0.0D, 0.35D, 0.0D, 0.0D);
        }
    }

}
