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

package com.favouriteless.enchanted.common.altar;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.power.IPowerConsumer;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.stateobserver.api.AbstractStateObserver;
import com.favouriteless.stateobserver.api.StateChangeSet.StateChange;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * StateObserver implementation for {@link AltarBlockEntity}. This is used to notify every nearby {@link IPowerConsumer}
 * of the {@link com.favouriteless.enchanted.api.power.IPowerProvider} near them. Changes to the power/upgrades are also calculated in this StateObserver.
 */
public class AltarStateObserver extends AbstractStateObserver {

    public AltarStateObserver(Level level, BlockPos pos, int xRadius, int yRadius, int zRadius) {
        super(level, pos, xRadius, yRadius, zRadius);
    }

    @Override
    protected void handleChanges() {
        if(!getLevel().isClientSide) {
            BlockEntity be = getLevel().getBlockEntity(getPos());
            if (be instanceof AltarBlockEntity altar) { // Only apply this StateObserver to altars.

                for (StateChange change : getStateChangeSet().getChanges()) { // For all changes
                    if (altar.posWithinRange(change.pos(), EnchantedConfig.ALTAR_RANGE.get())) { // Change is relevant
                        if(!change.oldState().is(change.newState().getBlock())) { // Block actually changed
                            if(getLevel().getBlockEntity(change.pos()) instanceof IPowerConsumer consumer)
                                consumer.getPosHolder().add(getPos()); // Subscribe power consumer to this Altar if present.

                            altar.removeBlock(change.oldState().getBlock());
                            altar.addBlock(change.newState().getBlock());
                        }
                        if(altar.posIsUpgrade(change.pos())) {
                            altar.removeUpgrade(change.oldState().getBlock());
                            altar.addUpgrade(change.newState().getBlock());
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onRemove() {

    }
}
