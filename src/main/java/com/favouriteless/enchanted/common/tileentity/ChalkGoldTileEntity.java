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

import com.favouriteless.enchanted.common.rituals.AbstractRitual;
import com.favouriteless.enchanted.core.init.EnchantedRituals;
import com.favouriteless.enchanted.core.init.EnchantedTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class ChalkGoldTileEntity extends TileEntity implements ITickableTileEntity {

    private boolean executing = false;
    private int ticks = 0;

    public ChalkGoldTileEntity(final TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public ChalkGoldTileEntity() {
        this(EnchantedTileEntities.CHALK_GOLD.get());
    }

    public void Execute(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!executing && !world.isClientSide) {
            executing = true;

            List<AbstractRitual> currentRituals = getRitualsFor(pos, world, player);

            if(!currentRituals.isEmpty()) {
                for(AbstractRitual ritual : currentRituals) {
                    ritual.startRitual(state, (ServerWorld)world, pos, player);
                }
            } else {
                world.playSound(null, pos.getX(), pos.getY() + 1, pos.getZ(), SoundEvents.NOTE_BLOCK_SNARE, SoundCategory.MASTER, 2f, 1f);
            }
        }
    }

    @Override
    public void tick() {
        if(executing) {
            ticks++;
            if(ticks >= 40) {
                ticks = 0;
                executing = false;
            }
        }
    }

    public static List<AbstractRitual> getRitualsFor(BlockPos pos, World world, PlayerEntity player) {

        String[] ritualGlyphs = new String[225];
        List<Entity> ritualEntities;
        BlockPos corner = pos.offset(-7, 0, -7);
        // GET GLYPHS ARRAY
        for (int z = 0; z < 15; z++) {
            for (int x = 0; x < 15; x++) {
                Block blockType = world.getBlockState(corner.offset(x, 0, z)).getBlock();
                String type = "XX";

                for(Block block : EnchantedRituals.CHARACTER_MAP.keySet())
                {
                    if (block == blockType) {
                        type = EnchantedRituals.CHARACTER_MAP.get(blockType);
                        break;
                    }
                }
                ritualGlyphs[(x + (z*15))] = type;
            }
        }
        // GET ENTITIES LIST
        ritualEntities = world.getEntities(player, new AxisAlignedBB(corner, corner.offset(15, 2, 15)));

        return EnchantedRituals.forData(ritualGlyphs, ritualEntities);
    }

}
