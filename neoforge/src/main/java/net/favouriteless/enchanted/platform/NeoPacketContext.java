package net.favouriteless.enchanted.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class NeoPacketContext implements PacketContext {

    private final IPayloadContext context;

    public NeoPacketContext(IPayloadContext context) {
        this.context = context;
    }

    @Override
    public Player getPlayer() {
        return context.player();
    }

    @Override
    public CompletableFuture<Void> enqueueWork(Runnable task) {
        return context.enqueueWork(task);
    }

    @Override
    public <T> CompletableFuture<T> enqueueWork(Supplier<T> task) {
        return context.enqueueWork(task);
    }

    @Override
    public <T extends CustomPacketPayload> void reply(T payload) {
        context.reply(payload);
    }

    @Override
    public void disconnect(Component reason) {
        context.disconnect(reason);
    }

}
