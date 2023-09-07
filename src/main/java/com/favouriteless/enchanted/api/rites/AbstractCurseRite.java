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

import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.FamiliarEntry;
import com.favouriteless.enchanted.common.curses.CurseManager;
import com.favouriteless.enchanted.common.curses.CurseType;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.sounds.SoundSource;

/**
 * Simple AbstractRite implementation for creating a curse
 */
public abstract class AbstractCurseRite extends AbstractRite {

    private final CurseType curseType;

    public AbstractCurseRite(RiteType<?> type, int power, CurseType curseType) {
        super(type, power, 0);
        this.curseType = curseType;
    }

    @Override
    protected void execute() {
        if(targetUUID == null)
            cancel();
        else {
            int casterLevel = 0;

            IFamiliarCapability cap = level.getServer().getPlayerList().getPlayer(casterUUID).getCapability(EnchantedCapabilities.FAMILIAR).orElse(null);
            FamiliarEntry familiarEntry = cap.getFamiliarFor(casterUUID);
            if(!familiarEntry.isDismissed() && familiarEntry.getType() == FamiliarTypes.CAT.get())
                casterLevel++;

            level.sendParticles(EnchantedParticles.CURSE_SEED.get(), pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            level.playSound(null, pos, EnchantedSoundEvents.CURSE_CAST.get(), SoundSource.MASTER, 1.5F, 1.0F);
            CurseManager.createCurse(level, curseType, targetUUID, casterUUID, casterLevel);
            stopExecuting();
        }
    }

}
