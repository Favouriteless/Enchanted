package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.network.packets.client.PoppetAnimationPayload;
import net.favouriteless.enchanted.common.network.packets.client.SinkingCursePayload;
import net.favouriteless.enchanted.platform.CommonServices;

public class EPackets {

    public static void register() {
        CommonServices.NETWORK.registerClient(PoppetAnimationPayload.TYPE, PoppetAnimationPayload.STREAM_CODEC, PoppetAnimationPayload::handle);
        CommonServices.NETWORK.registerClient(SinkingCursePayload.TYPE, SinkingCursePayload.STREAM_CODEC, SinkingCursePayload::handle);
    }

}
