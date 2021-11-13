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

package com.favouriteless.enchanted.common.observerlib.altar;

import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
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

    private final ObservableArea observableArea = new ObservableAreaBoundingBox(new AxisAlignedBB(
            -(AltarTileEntity.RANGE-1), -AltarTileEntity.RANGE, -(AltarTileEntity.RANGE-1),
            AltarTileEntity.RANGE+2, AltarTileEntity.RANGE, AltarTileEntity.RANGE+2));

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
                AltarTileEntity altar = (AltarTileEntity) te;
                for (BlockStateChangeSet.StateChange change : changeSet.getChanges()) { // For all changes
                    if (altar.posWithinRange(change.getAbsolutePosition())) { // Change is relevant
                        if(change.getOldState().getBlock() != change.getNewState().getBlock()) { // Block changed
                            altar.removePower(change.getOldState().getBlock());
                            altar.addPower(change.getNewState().getBlock());
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
