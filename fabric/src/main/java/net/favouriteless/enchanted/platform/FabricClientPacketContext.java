package net.favouriteless.enchanted.platform;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.Context;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FabricClientPacketContext implements PacketContext {

    private final ClientPlayNetworking.Context context;

    public FabricClientPacketContext(Context context) {
        this.context = context;
    }

    @Override
    public Player getPlayer() {
        return context.player();
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return CompletableFuture.runAsync(task, Minecraft.getInstance());
    }

    @Override
    public <T> CompletableFuture<T> enqueueWork(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, Minecraft.getInstance());
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
