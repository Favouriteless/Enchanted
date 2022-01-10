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

package com.favouriteless.enchanted.common.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.packets.CBroomstickSteerPacket;
import com.favouriteless.enchanted.common.packets.EnchantedPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class EnchantedPackets {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Enchanted.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	public static void register() {
		int id = 0;

		INSTANCE.registerMessage(id++, CBroomstickSteerPacket.class, CBroomstickSteerPacket::encode, CBroomstickSteerPacket::decode, CBroomstickSteerPacket::receiveMessage);
	}

	public static void messageNearbyPlayers(EnchantedPacket packet, ServerWorld world, Vector3d origin, double radius) {
		for (ServerPlayerEntity player : world.players()) {
			if (player.distanceToSqr(origin.x(), origin.y(), origin.z()) < radius * radius)
				messagePlayer(player, packet);
		}
	}

	public static void messagePlayer(ServerPlayerEntity player, EnchantedPacket packet) {
		if (player.connection != null)
			INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}

	public static void messageAllPlayers(EnchantedPacket packet) {
		INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
	}

	public static void messageServer(EnchantedPacket packet) {
		INSTANCE.sendToServer(packet);
	}

}
