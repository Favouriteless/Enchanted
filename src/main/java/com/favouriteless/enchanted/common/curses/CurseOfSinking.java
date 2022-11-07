/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.curses;

import com.favouriteless.enchanted.api.curses.AbstractCurse;
import com.favouriteless.enchanted.common.init.EnchantedCurseTypes;
import com.favouriteless.enchanted.common.init.EnchantedPackets;
import com.favouriteless.enchanted.common.network.packets.EnchantedSinkingCursePacket;
import net.minecraft.server.level.ServerLevel;

public class CurseOfSinking extends AbstractCurse {

	public boolean wasSwimming = false;
	public boolean wasFlying = false;

	public CurseOfSinking() {
		super(EnchantedCurseTypes.SINKING.get());
	}

	@Override
	protected void onTick() {
		level = 4;
		if(targetPlayer != null) {
			boolean isSwimming = targetPlayer.isInWater();
			boolean isFlying = targetPlayer.isFallFlying();

			if(isSwimming != wasSwimming || isFlying != wasFlying) {
				if(isSwimming)
					EnchantedPackets.sendToPlayer(new EnchantedSinkingCursePacket(-0.025D * (level + 1)), targetPlayer);
				else if(isFlying)
					EnchantedPackets.sendToPlayer(new EnchantedSinkingCursePacket(-0.05D * (level + 1)), targetPlayer);
				else
					EnchantedPackets.sendToPlayer(new EnchantedSinkingCursePacket(0.0D), targetPlayer);
				wasSwimming = isSwimming;
				wasFlying = isFlying;
			}
		}
	}

	@Override
	public void onRemove(ServerLevel level) {
		if(targetPlayer == null || targetPlayer.isRemoved())
			targetPlayer = level.getServer().getPlayerList().getPlayer(targetUUID);
		if(targetPlayer != null) {
			EnchantedPackets.sendToPlayer(new EnchantedSinkingCursePacket(0.0D), targetPlayer); // Reset the player's sinking when removed
		}
	}

}
