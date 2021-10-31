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

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.core.init.EnchantedTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class AltarTileEntity extends TileEntity implements ITickableTileEntity {

    public int maxPower;
    public int currentPower;
    public int rechargeMultiplier;

    private double powerMultiplier;

    private final AltarBlockData altarBlockData = new AltarBlockData();

    public AltarTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public AltarTileEntity() {
        this(EnchantedTileEntities.ALTAR.get());
    }

    @Override
    public void tick() {

    }

    static {
        AltarBlockData.addSource(Blocks.DRAGON_EGG, 250, 1);
        AltarBlockData.addSource(BlockTags.LEAVES, 3, 100);
    }

    public void updatePower() {
        recalculateBlocks();
    }

    public void recalculateBlocks() {
        if(level != null && !level.isClientSide) {
            BlockPos startingPos = this.getBlockPos().offset(-16, -16, -16);
            altarBlockData.reset();
            int newPower = 0;

            for (int x = 0; x < 32; x++) {
                for (int y = 0; y < 32; y++) {
                    for (int z = 0; z < 32; z++) {
                        newPower += altarBlockData.addBlock(level.getBlockState(startingPos.offset(x, y, z)).getBlock());
                    }
                }
            }
            maxPower = newPower;
            Enchanted.LOGGER.info(maxPower);
        }
    }

    public static class AltarBlockData {
        public static HashMap<Block, Point> BLOCKS = new HashMap<>();
        public static HashMap<INamedTag<Block>, Point> TAGS = new HashMap<>();
        public HashMap<Block, Integer> blocksAmount = new HashMap<>();
        public HashMap<INamedTag<Block>, Integer> tagsAmount = new HashMap<>();

        public AltarBlockData() {
            for(Block block : BLOCKS.keySet()) {
                blocksAmount.put(block, 0);
            }
            for(INamedTag<Block> tag : TAGS.keySet()) {
                tagsAmount.put(tag, 0);
            }
        }

        public static void addSource(Block block, int power, int max) {
            BLOCKS.put(block, new Point(power, max));
        }

        public static void addSource(INamedTag<Block> tag, int power, int max) {
            TAGS.put(tag, new Point(power, max));
        }

        public int addBlock(Block block) {
            Integer amount = blocksAmount.get(block);

            if(amount == null) { // Not in blocks
                ArrayList<INamedTag<Block>> tagKeys = new ArrayList<>(TAGS.keySet());

                for(INamedTag<Block> tag : tagKeys) { // For all tags
                    if(block.is(tag)) { // If block part of tag
                        amount = tagsAmount.get(tag);

                        Point tagValues = TAGS.get(tag);
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

            Point blockValues = BLOCKS.get(block);
            blocksAmount.replace(block, amount + 1);
            if(amount >= blockValues.y) { // Too many of block
                return 0;
            }
            else {
                return blockValues.x;
            }
        }

        public void reset() {
            blocksAmount.replaceAll((key,value) -> value = 0);
            tagsAmount.replaceAll((key,value) -> value = 0);
        }
    }
}
