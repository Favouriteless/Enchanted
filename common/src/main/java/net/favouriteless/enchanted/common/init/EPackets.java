package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.network.client.PoppetAnimationPayload;
import net.favouriteless.enchanted.platform.EServices;

public class EPackets {

    public static void register() {
        EServices.NETWORK.registerClient(PoppetAnimationPayload.TYPE, PoppetAnimationPayload.STREAM_CODEC, PoppetAnimationPayload::handle);
    }

}
