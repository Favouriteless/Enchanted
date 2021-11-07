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

import com.favouriteless.enchanted.common.blocks.AltarBlock;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.core.util.AltarPowerReloadListener;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AltarTileEntity extends TileEntity implements ITickableTileEntity {

    public static final int RANGE = 16;

    private int maxPower; // 0
    private int currentPower; // 1
    private int rechargeMultiplier; // 2

    public final AltarBlockData altarBlockData = new AltarBlockData();
    public final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return maxPower;
                case 1:
                    return currentPower;
                case 2:
                    return rechargeMultiplier;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    maxPower = value;
                    break;
                case 1:
                    currentPower = value;
                    break;
                case 2:
                    rechargeMultiplier = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    private boolean loaded = false;
    private Vector3d centerPos;
    private boolean facingX;

    public AltarTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public AltarTileEntity() {
        this(EnchantedTileEntities.ALTAR.get());
    }

    @Override
    public void tick() {
        if(level != null && !loaded) {
            facingX = level.getBlockState(worldPosition).getValue(AltarBlock.FACING_X);
            centerPos = facingX ?
                    Vector3d.atLowerCornerOf(worldPosition).add(1.0D, 0.0D, 0.5D) :
                    Vector3d.atLowerCornerOf(worldPosition).add(0.5D, 0.0D, 1.0D);

            recalculateBlocks();
            loaded = true;
        }
    }

    public void recalculateBlocks() {
        if(level != null && !level.isClientSide) {
            BlockPos startingPos = new BlockPos(centerPos.add(-(RANGE+2), -(RANGE+2), -(RANGE+2)));
            altarBlockData.reset();
            int newPower = 0;

            for (int x = 0; x < (RANGE+2)*2; x++) {
                for (int y = 0; y < (RANGE+2)*2; y++) {
                    for (int z = 0; z < (RANGE+2)*2; z++) {
                        if(posWithinRange(startingPos.offset(x, y, z))) {
                            newPower += altarBlockData.addBlock(level.getBlockState(startingPos.offset(x, y, z)).getBlock());
                        }
                    }
                }
            }
            maxPower = newPower;
            if(currentPower > maxPower) currentPower = maxPower;
        }
    }

    public boolean posWithinRange(BlockPos pos) {
        if(this.level != null) {
            double rx = facingX ? RANGE+1 : RANGE;
            double ry = RANGE;
            double rz = facingX ? RANGE : RANGE+1;
            double dx = pos.getX() - centerPos.x;
            double dy = pos.getY() - centerPos.y;
            double dz = pos.getZ() - centerPos.z;
            return (dx * dx) / (rx * rx) + (dy * dy) / (ry * ry) + (dz * dz) / (rz * rz) <= 1;
        }
        return false;
    }

    public void addPower(Block block) {
        this.maxPower += altarBlockData.addBlock(block);
    }

    public void removePower(Block block) {
        this.maxPower -= altarBlockData.removeBlock(block);
        if(currentPower > maxPower) currentPower = maxPower;
    }

    public static class AltarBlockData {
        public HashMap<Block, Integer> blocksAmount = new HashMap<>();
        public HashMap<IOptionalNamedTag<Block>, Integer> tagsAmount = new HashMap<>();

        public AltarBlockData() {
            for(Block block : AltarPowerReloadListener.INSTANCE.BLOCKS.keySet()) {
                blocksAmount.put(block, 0);
            }
            for(IOptionalNamedTag<Block> tag : AltarPowerReloadListener.INSTANCE.TAGS.keySet()) {
                tagsAmount.put(tag, 0);
            }
        }

        public int addBlock(Block block) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                ArrayList<IOptionalNamedTag<Block>> tagKeys = new ArrayList<>(AltarPowerReloadListener.INSTANCE.TAGS.keySet());

                for(IOptionalNamedTag<Block> tag : tagKeys) { // For all tags
                    if(block.is(tag)) { // If block part of tag
                        amount = tagsAmount.get(tag);

                        Point tagValues = AltarPowerReloadListener.INSTANCE.TAGS.get(tag);
                        tagsAmount.replace(tag, tagsAmount.get(tag) + 1);
                        if(amount >= tagValues.y) {
                            return 0;
                        }
                        else {
                            return tagValues.x;
                        }
                    }
                }
            }
            if(amount == null) {
                return 0;
            }

            Point blockValues = AltarPowerReloadListener.INSTANCE.BLOCKS.get(block);
            blocksAmount.replace(block, amount + 1);
            if(amount >= blockValues.y) { // Too many of block
                return 0;
            }
            else {
                return blockValues.x;
            }
        }

        public int removeBlock(Block block) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                ArrayList<IOptionalNamedTag<Block>> tagKeys = new ArrayList<>(AltarPowerReloadListener.INSTANCE.TAGS.keySet());

                for(IOptionalNamedTag<Block> tag : tagKeys) { // For all tags
                    if(block.is(tag)) { // If block part of tag
                        amount = tagsAmount.get(tag);

                        Point tagValues = AltarPowerReloadListener.INSTANCE.TAGS.get(tag);
                        tagsAmount.replace(tag, tagsAmount.get(tag) - 1);
                        if(amount <= tagValues.y) {
                            return tagValues.x;
                        }
                        else {
                            return 0;
                        }
                    }
                }
            }
            if(amount == null) {
                return 0;
            }

            Point blockValues = AltarPowerReloadListener.INSTANCE.BLOCKS.get(block);
            blocksAmount.replace(block, amount - 1);
            if(amount <= blockValues.y) { // Too many of block
                return blockValues.x;
            }
            else {
                return 0;
            }
        }

        public void reset() {
            blocksAmount.replaceAll((key,value) -> value = 0);
            tagsAmount.replaceAll((key,value) -> value = 0);
        }
    }
}
