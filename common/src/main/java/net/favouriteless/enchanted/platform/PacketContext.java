package net.favouriteless.enchanted.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface PacketContext {

    Player getPlayer();

    CompletableFuture<Void> enqueueWork(Runnable task);

    <T> CompletableFuture<T> enqueueWork(Supplier<T> task);

    <T extends CustomPacketPayload> void reply(T payload);

    void disconnect(Component reason);

}
