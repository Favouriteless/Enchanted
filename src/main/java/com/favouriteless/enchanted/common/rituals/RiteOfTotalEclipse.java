/*
 * Copyright (c) 2021. Favouriteless
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

package com.favouriteless.enchanted.common.rituals;

import com.favouriteless.enchanted.core.init.EnchantedItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import java.util.List;

public class RiteOfTotalEclipse extends AbstractRitual {

    // Do not use this constructor
    public RiteOfTotalEclipse(List<Entity> entitiesNeeded) { ENTITIES_TO_KILL = entitiesNeeded; }

    public RiteOfTotalEclipse() {
        GLYPHS_REQUIRED = new String[] { // 2D representation of chalk circles. X = anything, A = air, W = white chalk, R = red chalk, P = purple chalk, G = gold chalk (must be in center)
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "W", "W", "W", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "W", "X", "X", "X", "W", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "X", "X", "X", "X", "X", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "X", "X", "G", "X", "X", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "W", "X", "X", "X", "X", "X", "W", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "W", "X", "X", "X", "W", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "W", "W", "W", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X",
                "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"
        };

        ITEMS_REQUIRED.put(EnchantedItems.QUICKLIME.get(), 1);
        ITEMS_REQUIRED.put(Items.STONE_AXE, 1);
    }

    @Override
    public void execute() {
        world.sendParticles(ParticleTypes.WITCH, pos.getX(), pos.getY() + 1, pos.getZ(), 200, 1, 1, 1, 0);
        world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundCategory.MASTER, 1f, 1f);
        world.setDayTime((long)14000);

        activating = false;
        isExecutingEffect = false;
    }

    @Override
    protected void onTick() {
        // Do tick based ritual effects here
    }

}
