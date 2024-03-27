package com.favouriteless.enchanted.common.network;

import com.favouriteless.enchanted.common.network.packets.EnchantedPoppetAnimationPacket;
import com.favouriteless.enchanted.common.network.packets.EnchantedSinkingCursePacket;
import com.favouriteless.enchanted.platform.Services;

public class EnchantedPackets {

    public static void register() {
        Services.NETWORK.register("poppet_animation", EnchantedPoppetAnimationPacket.class, EnchantedPoppetAnimationPacket::decode);
        Services.NETWORK.register("sinking", EnchantedSinkingCursePacket.class, EnchantedSinkingCursePacket::decode);
    }

}
