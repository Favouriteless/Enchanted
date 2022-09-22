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

package com.favouriteless.enchanted.common.multiblock.observerlib.altar;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.init.EnchantedTags;
import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import com.favouriteless.enchanted.api.altar.IAltarPowerConsumer;
import hellfirepvp.observerlib.api.ChangeObserver;
import hellfirepvp.observerlib.api.ObservableArea;
import hellfirepvp.observerlib.api.ObservableAreaBoundingBox;
import hellfirepvp.observerlib.api.block.BlockChangeSet;
import hellfirepvp.observerlib.common.change.BlockStateChangeSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class AltarChangeObserver extends ChangeObserver {

    private final int range = EnchantedConfig.ALTAR_RANGE.get();
    private final ObservableArea observableArea = new ObservableAreaBoundingBox(new AxisAlignedBB(
            -(range-1), -range, -(range-1),
            range+2, range, range+2));

    public AltarChangeObserver(ResourceLocation providerRegistryName) {
        super(providerRegistryName);
    }

    @Override
    public void initialize(IWorld world, BlockPos center) {
    }

    @Nonnull
    @Override
    public ObservableArea getObservableArea() {
        return observableArea;
    }

    @Override
    public boolean notifyChange(World world, BlockPos center, BlockChangeSet changeSet) {
        if(!world.isClientSide) {
            TileEntity te = world.getBlockEntity(center);
            if (te instanceof AltarTileEntity) {
                AltarTileEntity altar = (AltarTileEntity)te;

                for (BlockStateChangeSet.StateChange change : changeSet.getChanges()) { // For all changes
                    if (altar.posWithinRange(change.getAbsolutePosition(), range)) { // Change is relevant
                        if(!change.getOldState().is(change.getNewState().getBlock())) { // Block changed
                            if(change.getNewState().getBlock().is(EnchantedTags.POWER_CONSUMERS)) {
                                altar.addConsumer((IAltarPowerConsumer) world.getBlockEntity(change.getAbsolutePosition()));
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
    public void readFromNBT(CompoundNBT tag) {

    }

    @Override
    public void writeToNBT(CompoundNBT tag) {

    }
}
