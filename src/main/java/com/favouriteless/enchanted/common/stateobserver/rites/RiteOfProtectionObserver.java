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

package com.favouriteless.enchanted.common.stateobserver.rites;

import com.favouriteless.stateobserver.api.AbstractStateObserver;
import com.favouriteless.stateobserver.api.StateChangeSet.StateChange;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class RiteOfProtectionObserver extends AbstractStateObserver {

	public final Block block;

	public RiteOfProtectionObserver(Level level, BlockPos pos, int radiusX, int radiusY, int radiusZ, Block block) {
		super(level, pos, radiusX, radiusY, radiusZ);
		this.block = block;
	}

	@Override
	protected void handleChanges() {
		if(!getLevel().isClientSide) {
			for(StateChange change : getStateChangeSet().getChanges()) { // For all changes
				if(change.oldState().getBlock() == block) {
					if(change.newState().getBlock() != block) {
						getLevel().setBlockAndUpdate(change.pos(), change.oldState());
					}
				}
			}
		}
	}

	@Override
	public void onInit() {

	}

	@Override
	public void onRemove() {

	}

}
