package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.common.init.EnchantedKeybinds;

public class EnchantedClient {

    public static void init() {
        ClientRegistry.register();
        EnchantedKeybinds.load();
    }

}
