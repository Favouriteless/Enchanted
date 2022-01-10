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

package com.favouriteless.enchanted.common.packets;

import com.favouriteless.enchanted.common.entities.BroomstickEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class CBroomstickSteerPacket implements EnchantedPacket {
	public final boolean up;
	public final boolean down;
	public final boolean left;
	public final boolean right;

	public CBroomstickSteerPacket(boolean up, boolean down, boolean left, boolean right) {
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeBoolean(up);
		buffer.writeBoolean(down);
		buffer.writeBoolean(left);
		buffer.writeBoolean(right);
	}

	public static CBroomstickSteerPacket decode(PacketBuffer buffer) {
		return new CBroomstickSteerPacket(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
	}

	@Override
	public void receiveMessage(Supplier<Context> context) {
		Entity entity = context.get().getSender().getVehicle();
		if(entity instanceof BroomstickEntity) {
			BroomstickEntity broomstick = (BroomstickEntity)entity;
			broomstick.setInput(up, down, left, right);
		}
		context.get().setPacketHandled(true);
	}
}
