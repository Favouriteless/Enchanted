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

package com.favouriteless.enchanted.api.rites;

import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedSoundEvents;
import com.favouriteless.enchanted.common.util.curse.CurseManager;
import com.favouriteless.enchanted.common.util.curse.CurseType;
import com.favouriteless.enchanted.common.util.rite.RiteType;
import net.minecraft.sounds.SoundSource;

/**
 * Simple AbstractRite implementation for creating a curse
 */
public abstract class AbstractCurseRite extends AbstractRite {

    private final CurseType<?> curseType;

    public AbstractCurseRite(RiteType<?> type, int power, CurseType<?> curseType) {
        super(type, power, 0);
        this.curseType = curseType;
    }

    @Override
    protected void execute() {
        if(targetUUID == null)
            cancel();
        else {
            level.sendParticles(EnchantedParticles.CURSE_SEED.get(), pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            level.playSound(null, pos, EnchantedSoundEvents.CURSE_CAST.get(), SoundSource.MASTER, 1.5F, 1.0F);
            CurseManager.createCurse(level, curseType, targetUUID, casterUUID, 0);
            stopExecuting();
        }
    }

}
