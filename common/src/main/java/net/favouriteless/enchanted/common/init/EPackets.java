package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.network.client.PoppetAnimationPayload;
import net.favouriteless.enchanted.common.network.client.flags.SyncedFloatPayload;
import net.favouriteless.enchanted.platform.CommonServices;

public class EPackets {

    public static void register() {
        CommonServices.NETWORK.registerClient(PoppetAnimationPayload.TYPE, PoppetAnimationPayload.STREAM_CODEC, PoppetAnimationPayload::handle);
        CommonServices.NETWORK.registerClient(SyncedFloatPayload.TYPE, SyncedFloatPayload.STREAM_CODEC, SyncedFloatPayload::handle);
    }

}
