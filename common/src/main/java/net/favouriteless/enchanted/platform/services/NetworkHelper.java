package net.favouriteless.enchanted.platform.services;

import net.favouriteless.enchanted.platform.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.function.BiConsumer;

public interface NetworkHelper {

    /**
     * Register a custom payload to be received by the client.
     */
    <T extends CustomPacketPayload> void registerClient(CustomPacketPayload.Type<T> type,
                                                        StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                        BiConsumer<T, PacketContext> handler);

    /**
     * Register a custom payload to be received by the server.
     */
    <T extends CustomPacketPayload> void registerServer(CustomPacketPayload.Type<T> type,
                                                        StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                        BiConsumer<T, PacketContext> handler);

    /**
     * Register a custom payload to be received bidirectionally.
     */
    <T extends CustomPacketPayload> void registerBidirectional(CustomPacketPayload.Type<T> type,
                                                               StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                               BiConsumer<T, PacketContext> handler);

    /**
     * Attempt to send a packet to a specific player FROM the server.
     *
     * @param payload {@link CustomPacketPayload} to be sent.
     * @param player  {@link ServerPlayer} receiving the packet.
     */
    void sendToPlayer(CustomPacketPayload payload, ServerPlayer player);

    /**
     * Attempt to send a packet to all players FROM the server.
     *
     * @param payload {@link CustomPacketPayload} to be sent.
     * @param server  Access to {@link MinecraftServer} for Fabric to grab players from.
     */
    void sendToAllPlayers(CustomPacketPayload payload, MinecraftServer server);

    /**
     * Attempt to send a packet to the server FROM a client.
     *
     * @param payload The {@link CustomPacketPayload} to be sent.
     */
    void sendToServer(CustomPacketPayload payload);

    /**
     * Send a packet to all players tracking the given entity.
     *
     * @param payload {@link CustomPacketPayload} to be sent.
     * @param entity  Entity being tracked.
     */
    void sendToTracking(CustomPacketPayload payload, Entity entity);

}
