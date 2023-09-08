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

package com.favouriteless.enchanted.common.altar;

import com.favouriteless.enchanted.api.power.IPowerConsumer.IPowerPosHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link IPowerPosHolder} implementation which stores
 * the provided {@link BlockPos} by their distance from the holder.
 */
public class SimplePowerPosHolder implements IPowerPosHolder {

	private final List<BlockPos> altars = new ArrayList<>();
	private final BlockPos pos;

	public SimplePowerPosHolder(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public List<BlockPos> getPositions() {
		return altars;
	}

	@Override
	public void remove(BlockPos altarPos) {
		altars.remove(altarPos);
	}

	@Override
	public void add(BlockPos altarPos) {
		if(altars.isEmpty()) {
			altars.add(altarPos);
		}
		else {
			if(!altars.contains(pos)) {
				for(int i = 0; i < altars.size(); i++) {
					if(pos.distSqr(altarPos) < pos.distSqr(altars.get(i))) {
						altars.add(i, altarPos);
						return;
					}
					else if(i == altars.size() - 1) {
						altars.add(altarPos);
						return;
					}
				}
			}
		}
	}

	@Override
	public ListTag serializeNBT() {
		ListTag nbt = new ListTag();
		for(BlockPos pos : altars) {
			CompoundTag posTag = new CompoundTag();
			posTag.putInt("x", pos.getX());
			posTag.putInt("y", pos.getY());
			posTag.putInt("z", pos.getZ());
			nbt.add(posTag);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(ListTag nbt) {
		for(Tag tag : nbt) {
			CompoundTag posTag = (CompoundTag)tag;
			altars.add(new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
		}
	}

}
