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

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.common.init.EnchantedRiteTypes;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.rites.AbstractRite;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ChalkGoldTileEntity extends TileEntity implements ITickableTileEntity {

    private AbstractRite currentRite = null;

    public ChalkGoldTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ChalkGoldTileEntity() {
        this(EnchantedTileEntities.CHALK_GOLD.get());
    }

    public void execute(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!world.isClientSide) {
            if (currentRite == null) {

                AbstractRite rite = EnchantedRiteTypes.riteFor(world, pos);

                if (rite != null) {
                    currentRite = rite.create();
                    currentRite.setWorld(world);
                    currentRite.setPos(pos);
                    currentRite.setCaster(player);
                    currentRite.start();
                } else {
                    world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.NOTE_BLOCK_SNARE, SoundCategory.MASTER, 2f, 1f);
                }
            }
        }
    }

    public void clearRite() {
        this.currentRite = null;
    }

    @Override
    public void tick() {
        if(currentRite != null) {
            currentRite.tick();
        }
    }

}
