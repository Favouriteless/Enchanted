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
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
import com.favouriteless.enchanted.common.curses.CurseManager;
import com.favouriteless.enchanted.common.curses.CurseType;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * Simple {@link AbstractRite} implementation for creating a curse and applying it to a player.
 *
 * <p>IMPORTANT: Rites implementing this should add a filled taglock to their requirements so the target's UUID can
 * be grabbed.</p>
 */
public abstract class AbstractCurseRite extends AbstractRite {

    private final CurseType curseType;

    public AbstractCurseRite(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power, CurseType curseType) {
        super(type, level, pos, caster, power, 0);
        this.curseType = curseType;
    }

    @Override
    protected void execute() {
        if(getTargetUUID() == null)
            cancel();
        else {
            int casterLevel = 0;


            IFamiliarCapability cap = getLevel().getServer().getLevel(Level.OVERWORLD).getCapability(EnchantedCapabilities.FAMILIAR).orElse(null);
            IFamiliarEntry familiarEntry = cap.getFamiliarFor(getCasterUUID());
            if(!familiarEntry.isDismissed() && familiarEntry.getType() == FamiliarTypes.CAT.get())
                casterLevel++;

            BlockPos pos = getPos();
            getLevel().sendParticles(EnchantedParticles.CURSE_SEED.get(), pos.getX()+0.5D, pos.getY(), pos.getZ()+0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            getLevel().playSound(null, pos, EnchantedSoundEvents.CURSE_CAST.get(), SoundSource.MASTER, 1.5F, 1.0F);
            CurseManager.createCurse(getLevel(), curseType, getTargetUUID(), getCasterUUID(), casterLevel);
            stopExecuting();
        }
    }

}
