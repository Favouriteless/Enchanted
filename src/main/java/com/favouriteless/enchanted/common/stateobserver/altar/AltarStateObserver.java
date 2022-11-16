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

package com.favouriteless.enchanted.common.stateobserver.altar;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.stateobserver.api.AbstractStateObserver;
import com.favouriteless.stateobserver.api.StateChangeSet.StateChange;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class AltarStateObserver extends AbstractStateObserver {

    public AltarStateObserver(Level level, BlockPos pos, int radiusX, int radiusY, int radiusZ) {
        super(level, pos, radiusX, radiusY, radiusZ);
    }

    @Override
    protected void handleChanges() {
        if(!getLevel().isClientSide) {
            BlockEntity be = getLevel().getBlockEntity(getPos());
            if (be instanceof AltarBlockEntity altar) {

                for (StateChange change : getStateChangeSet().getChanges()) { // For all changes
                    if (altar.posWithinRange(change.pos(), EnchantedConfig.ALTAR_RANGE.get())) { // Change is relevant
                        if(!change.oldState().is(change.newState().getBlock())) { // Block changed
                            if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.Blocks.POWER_CONSUMERS).contains(change.newState().getBlock())) {
                                altar.addConsumer((IAltarPowerConsumer) getLevel().getBlockEntity(change.pos()));
                            }
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
