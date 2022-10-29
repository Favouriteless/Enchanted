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

package com.favouriteless.enchanted.common.observerlib.altar;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import hellfirepvp.observerlib.api.ChangeObserver;
import hellfirepvp.observerlib.api.ObservableArea;
import hellfirepvp.observerlib.api.ObservableAreaBoundingBox;
import hellfirepvp.observerlib.api.block.BlockChangeSet;
import hellfirepvp.observerlib.common.change.BlockStateChangeSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class AltarChangeObserver extends ChangeObserver {

    private final int range = EnchantedConfig.ALTAR_RANGE.get();
    private final ObservableArea observableArea = new ObservableAreaBoundingBox(new AABB(
            -(range-1), -range, -(range-1),
            range+2, range, range+2));

    public AltarChangeObserver(ResourceLocation providerRegistryName) {
        super(providerRegistryName);
    }

    @Override
    public void initialize(LevelAccessor world, BlockPos center) {
    }

    @Nonnull
    @Override
    public ObservableArea getObservableArea() {
        return observableArea;
    }

    @Override
    public boolean notifyChange(Level level, BlockPos center, BlockChangeSet changeSet) {
        if(!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(center);
            if (be instanceof AltarBlockEntity altar) {

                for (BlockStateChangeSet.StateChange change : changeSet.getChanges()) { // For all changes
                    if (altar.posWithinRange(change.getAbsolutePosition(), range)) { // Change is relevant
                        if(!change.getOldState().is(change.getNewState().getBlock())) { // Block changed
                            if(ForgeRegistries.BLOCKS.tags().getTag(EnchantedTags.POWER_CONSUMERS).contains(change.getNewState().getBlock())) {
                                altar.addConsumer((IAltarPowerConsumer) level.getBlockEntity(change.getAbsolutePosition()));
                            }
                            altar.removePower(change.getOldState().getBlock());
                            altar.addPower(change.getNewState().getBlock());
                        }
                        if(altar.posIsUpgrade(change.getAbsolutePosition())) {
                            altar.recalculateUpgrades();
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void readFromNBT(CompoundTag tag) {

    }

    @Override
    public void writeToNBT(CompoundTag tag) {

    }
}
