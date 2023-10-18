package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.common.init.EnchantedKeybinds;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;

public class EnchantedClient {

    public static void init() {
        ClientRegistry.register();
        EnchantedBlocks.initRender();
        EnchantedKeybinds.registerKeybinds();
    }

}
