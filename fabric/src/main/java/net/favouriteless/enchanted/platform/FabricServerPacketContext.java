package net.favouriteless.enchanted.platform;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.Context;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FabricServerPacketContext implements PacketContext {

    private final ServerPlayNetworking.Context context;

    public FabricServerPacketContext(Context context) {
        this.context = context;
    }

    @Override
    public Player getPlayer() {
        return context.player();
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return CompletableFuture.runAsync(task, context.server());
    }

    @Override
    public <T> CompletableFuture<T> enqueueWork(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, context.server());
    }

    @Override
    public <T extends CustomPacketPayload> void reply(T payload) {
        context.responseSender().sendPacket(payload);
    }

    @Override
    public void disconnect(Component reason) {
        context.responseSender().disconnect(reason);
    }

}
