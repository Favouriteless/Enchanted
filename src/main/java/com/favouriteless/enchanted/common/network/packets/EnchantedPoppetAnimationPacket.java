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

package com.favouriteless.enchanted.common.network.packets;

import com.favouriteless.enchanted.client.render.poppet.PoppetAnimationManager;
import com.favouriteless.enchanted.common.items.poppets.PoppetUtils.PoppetResult;
import com.favouriteless.enchanted.common.network.EnchantedPacket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class EnchantedPoppetAnimationPacket implements EnchantedPacket {

	private final PoppetResult result;
	private final ItemStack item;

	public EnchantedPoppetAnimationPacket(PoppetResult result, ItemStack itemStack) {
		this.result = result;
		this.item = itemStack;
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeEnum(result);
		buffer.writeItem(item);
	}

	public static EnchantedPoppetAnimationPacket decode(PacketBuffer buffer) {
		return new EnchantedPoppetAnimationPacket(buffer.readEnum(PoppetResult.class), buffer.readItem());
	}

	@Override
	public void handle(Supplier<Context> context) {
		PoppetAnimationManager.startAnimation(result, item);
		context.get().setPacketHandled(true);
	}

}
