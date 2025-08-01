package net.favouriteless.enchanted.common.network.client.flags;

import io.netty.buffer.ByteBuf;
import net.favouriteless.enchanted.common.SyncedFlags;
import net.favouriteless.enchanted.common.SyncedFlags.Flag;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.PacketContext;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncedFloatPayload(Flag<Float> flag, Float value) implements CustomPacketPayload {

    public static final Type<SyncedFloatPayload> TYPE = new Type<>(Enchanted.id("synced_float"));

    @SuppressWarnings("unchecked")
    public static final StreamCodec<ByteBuf, SyncedFloatPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.map(SyncedFlags::getFlag, SyncedFlags::getId), p -> p.flag,
            ByteBufCodecs.FLOAT, p -> p.value,
            (flag, value) -> new SyncedFloatPayload((Flag<Float>)flag, value) // Unchecked cast is fine, packet can only be serialized using the correct id anyway.
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncedFloatPayload payload, PacketContext context) {
        context.enqueueWork(() -> SyncedFlags.set(payload.flag, payload.value));
    }


}
