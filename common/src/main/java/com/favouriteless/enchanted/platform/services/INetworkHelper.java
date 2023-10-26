package com.favouriteless.enchanted.platform.services;

import com.favouriteless.enchanted.common.network.EnchantedPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public interface INetworkHelper {

    /**
     * Register a packet.
     *
     * @param name The name of the packet, only used on Fabric.
     * @param clazz The class of the packet being registered.
     * @param constructor {@link Function} returning a new instance of the packet.
     */
    <T extends EnchantedPacket> void register(String name, Class<T> clazz, Function<FriendlyByteBuf, T> constructor);

    /**
     * Attempt to send a packet to a specific player FROM the server.
     *
     * @param packet The {@link EnchantedPacket} to be sent.
     * @param player The {@link ServerPlayer} receiving the packet.
     */
    void sendToPlayer(EnchantedPacket packet, ServerPlayer player);

    /**
     * Attempt to send a packet to all players FROM the server.
     *
     * @param packet The {@link EnchantedPacket} to be sent.
     * @param server Access to {@link MinecraftServer} for Fabric to grab players from.
     */
    void sendToAllPlayers(EnchantedPacket packet, MinecraftServer server);

    /**
     * Attempt to send a packet to the server FROM a client.
     *
     * @param packet The {@link EnchantedPacket} to be sent.
     */
    void sendToServer(EnchantedPacket packet);

}
