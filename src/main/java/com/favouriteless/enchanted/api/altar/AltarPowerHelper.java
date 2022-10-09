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

package com.favouriteless.enchanted.api.altar;

import com.favouriteless.enchanted.common.tileentity.AltarTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class AltarPowerHelper {

	public static AltarTileEntity tryGetAltar(World world, List<BlockPos> potentialAltars) {
		while(!potentialAltars.isEmpty()) {
			if(world != null) {
				BlockPos altarPos = potentialAltars.get(0);
				TileEntity te = world.getBlockEntity(altarPos);
				if(te instanceof AltarTileEntity) {
					return (AltarTileEntity) te;
				}
				else {
					potentialAltars.remove(altarPos);
				}
			}
		}
		return null;
	}

	public static CompoundNBT savePosTag(List<BlockPos> potentialAltars, CompoundNBT nbt) {
		ListNBT listNbt = new ListNBT();

		for(BlockPos pos : potentialAltars) {
			CompoundNBT elementNbt = new CompoundNBT();
			elementNbt.putInt("xPos", pos.getX());
			elementNbt.putInt("yPos", pos.getY());
			elementNbt.putInt("zPos", pos.getZ());
			listNbt.add(elementNbt);
		}

		nbt.put("altarPos", listNbt);
		return nbt;
	}

	public static void loadPosTag(List<BlockPos> potentialAltars, CompoundNBT nbt) {
		if(nbt.contains("altarPos")) {
			potentialAltars.clear();
			ListNBT posList = (ListNBT)nbt.get("altarPos");

			for(INBT inbt : posList) {
				CompoundNBT posNbt = (CompoundNBT)inbt;
				potentialAltars.add(new BlockPos(posNbt.getInt("xPos"), posNbt.getInt("yPos"), posNbt.getInt("zPos")));
			}
		}
	}

	public static void addAltarByClosest(List<BlockPos> potentialAltars, World world, BlockPos pos, BlockPos altarPos) {
		if(world != null) {
			TileEntity te = world.getBlockEntity(altarPos);
			if(te instanceof AltarTileEntity) {
				AltarTileEntity altar = (AltarTileEntity) te;
				if(potentialAltars.isEmpty()) {
					potentialAltars.add(altarPos);
				}
				else if(!potentialAltars.contains(altarPos)) {
					for(int i = 0; i < potentialAltars.size(); i++) {

						TileEntity te1 = world.getBlockEntity(potentialAltars.get(i));

						if(te1 instanceof AltarTileEntity) {
							AltarTileEntity currentAltar = (AltarTileEntity) te1;

							if(altar.distanceTo(pos) < currentAltar.distanceTo(pos)) {
								potentialAltars.add(i, altarPos);
								break;
							}
							else if(i == potentialAltars.size() - 1) {
								potentialAltars.add(altarPos);
								break;
							}
						}
					}
				}
			}
		}
	}

}
