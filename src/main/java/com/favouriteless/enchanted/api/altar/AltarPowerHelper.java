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

package com.favouriteless.enchanted.api.altar;

import com.favouriteless.enchanted.common.blockentities.AltarBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public class AltarPowerHelper {

	public static AltarBlockEntity tryGetAltar(Level level, List<BlockPos> potentialAltars) {
		while(!potentialAltars.isEmpty()) {
			if(level != null) {
				BlockPos altarPos = potentialAltars.get(0);
				BlockEntity te = level.getBlockEntity(altarPos);
				if(te instanceof AltarBlockEntity) {
					return (AltarBlockEntity) te;
				}
				else {
					potentialAltars.remove(altarPos);
				}
			}
		}
		return null;
	}

	public static CompoundTag savePosTag(List<BlockPos> potentialAltars, CompoundTag nbt) {
		ListTag listNbt = new ListTag();

		for(BlockPos pos : potentialAltars) {
			CompoundTag elementNbt = new CompoundTag();
			elementNbt.putInt("xPos", pos.getX());
			elementNbt.putInt("yPos", pos.getY());
			elementNbt.putInt("zPos", pos.getZ());
			listNbt.add(elementNbt);
		}

		nbt.put("altarPos", listNbt);
		return nbt;
	}

	public static void loadPosTag(List<BlockPos> potentialAltars, CompoundTag nbt) {
		if(nbt.contains("altarPos")) {
			potentialAltars.clear();
			ListTag posList = (ListTag)nbt.get("altarPos");

			for(Tag inbt : posList) {
				CompoundTag posNbt = (CompoundTag)inbt;
				potentialAltars.add(new BlockPos(posNbt.getInt("xPos"), posNbt.getInt("yPos"), posNbt.getInt("zPos")));
			}
		}
	}

}
