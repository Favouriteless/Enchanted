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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.api.capabilities.EnchantedCapabilities;
import com.favouriteless.enchanted.api.curses.AbstractCurse;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability;
import com.favouriteless.enchanted.api.familiars.IFamiliarCapability.IFamiliarEntry;
import com.favouriteless.enchanted.common.curses.CurseManager;
import com.favouriteless.enchanted.common.curses.CurseSavedData;
import com.favouriteless.enchanted.common.curses.CurseType;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.EnchantedSoundEvents;
import com.favouriteless.enchanted.common.init.registry.FamiliarTypes;
import com.favouriteless.enchanted.common.rites.RiteType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

/**
 * Simple {@link AbstractRite} implementation for removing a curse from a player.
 *
 * <p>IMPORTANT: Rites implementing this should add a filled taglock to their requirements so the target's UUID can
 * be grabbed.</p>
 */
public abstract class AbstractRemoveCurseRite extends AbstractRite {

    public static final int RAISE = 300;
    public static final int START_SOUND = 190;
    private final CurseType curseType;

    public AbstractRemoveCurseRite(RiteType<?> type, ServerLevel level, BlockPos pos, UUID caster, int power, int powerTick, CurseType curseType) {
        super(type, level, pos, caster, power, powerTick);
        this.curseType = curseType;
    }

    @Override
    protected void execute() {
        if(getTargetUUID() == null)
            cancel();
        else {
            BlockPos pos = getPos();
            getLevel().sendParticles(EnchantedParticles.REMOVE_CURSE_SEED.get(), pos.getX() + 0.5D, pos.getY() + 2.5D, pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onTick() {
        ServerLevel level = getLevel();
        BlockPos pos = getPos();
        if(ticks == START_SOUND) {
            level.playSound(null, pos, EnchantedSoundEvents.REMOVE_CURSE.get(), SoundSource.MASTER, 1.0F, 1.0F);
        }
        else if(ticks == RAISE) {
            List<AbstractCurse> curses = CurseSavedData.get(level).curses.get(getTargetUUID());
            if(curses != null) {
                int casterLevel = 0;

                IFamiliarCapability cap = level.getServer().getLevel(Level.OVERWORLD).getCapability(EnchantedCapabilities.FAMILIAR).orElse(null);
                IFamiliarEntry familiarEntry = cap.getFamiliarFor(getCasterUUID());
                if(!familiarEntry.isDismissed() && familiarEntry.getType() == FamiliarTypes.CAT.get())
                    casterLevel++;


                for(AbstractCurse curse : curses) {
                    if(curse.getType() == curseType) {
                        int diff = casterLevel - curse.getLevel();

                        double cureChance = 1.0D + (diff * 0.2D); // If the caster is equal level, there is an 100% chance the curse will be cured.

                        if(Enchanted.RANDOM.nextDouble() < cureChance)
                            CurseManager.removeCurse(level, curse);
                        else if(curse.getLevel() < CurseManager.MAX_LEVEL)
                            curse.setLevel(curse.getLevel() + 1);
                        break;
                    }
                }
            }
            stopExecuting();
        }
    }

}
