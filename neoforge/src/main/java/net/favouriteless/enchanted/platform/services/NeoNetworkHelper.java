package net.favouriteless.enchanted.platform.services;

import net.favouriteless.enchanted.platform.PacketContext;
import net.favouriteless.enchanted.platform.NeoPacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class NeoNetworkHelper implements NetworkHelper {

    private static final List<PayloadRegisterable<?>> clientPackets = new ArrayList<>();
    private static final List<PayloadRegisterable<?>> serverPackets = new ArrayList<>();
    private static final List<PayloadRegisterable<?>> bidirectionalPackets = new ArrayList<>();

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        clientPackets.forEach(r -> r.playClient(registrar));
        serverPackets.forEach(r -> r.playClient(registrar));
        bidirectionalPackets.forEach(r -> r.playClient(registrar));
    }

    @Override
    public <T extends CustomPacketPayload> void registerClient(Type<T> type,
                                                               StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                               BiConsumer<T, PacketContext> handler) {
        clientPackets.add(new PayloadRegisterable<>(type, codec, handler));
    }

    @Override
    public <T extends CustomPacketPayload> void registerServer(Type<T> type,
                                                               StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                               BiConsumer<T, PacketContext> handler) {
        serverPackets.add(new PayloadRegisterable<>(type, codec, handler));
    }

    @Override
    public <T extends CustomPacketPayload> void registerBidirectional(Type<T> type,
                                                                      StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
                                                                      BiConsumer<T, PacketContext> handler) {
        bidirectionalPackets.add(new PayloadRegisterable<>(type, codec, handler));
    }

    @Override
    public void sendToPlayer(CustomPacketPayload payload, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Override
    public void sendToAllPlayers(CustomPacketPayload payload, MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(payload);
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    private record PayloadRegisterable<T extends CustomPacketPayload>(Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, BiConsumer<T, PacketContext> handler) {

        private void playClient(PayloadRegistrar registrar) {
            registrar.playToClient(type, codec, (payload, context) -> handler.accept(payload, new NeoPacketContext(context)));
        }

        private void playServer(PayloadRegistrar registrar) {
            registrar.playToServer(type, codec, (payload, context) -> handler.accept(payload, new NeoPacketContext(context)));
        }

        private void playBidirectional(PayloadRegistrar registrar) {
            registrar.playBidirectional(type, codec, (payload, context) -> handler.accept(payload, new NeoPacketContext(context)));
        }

    }

}
