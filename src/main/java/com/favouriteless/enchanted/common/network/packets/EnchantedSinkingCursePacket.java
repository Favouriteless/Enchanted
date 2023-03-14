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

package com.favouriteless.enchanted.common.network.packets;

import com.favouriteless.enchanted.client.client_handlers.misc.EnchantedClientValues;
import com.favouriteless.enchanted.common.network.EnchantedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class EnchantedSinkingCursePacket implements EnchantedPacket {

	private final double sinkingFactor;

	public EnchantedSinkingCursePacket(double sinkingFactor) {
		this.sinkingFactor = sinkingFactor;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeDouble(sinkingFactor);
	}

	public static EnchantedSinkingCursePacket decode(FriendlyByteBuf buffer) {
		return new EnchantedSinkingCursePacket(buffer.readDouble());
	}

	@Override
	public void handle(Supplier<Context> context) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.level != null && mc.level.isClientSide) {
			EnchantedClientValues.CURSE_SINKING_SPEED = sinkingFactor;
		}
		context.get().setPacketHandled(true);
	}

}
