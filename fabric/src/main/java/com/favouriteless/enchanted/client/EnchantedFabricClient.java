package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.client.events.ClientEventsFabric;
import com.favouriteless.enchanted.platform.FabricClientRegistryHelper;
import net.fabricmc.api.ClientModInitializer;

public class EnchantedFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new FabricClientRegistryHelper();
        EnchantedClient.init();
        FabricClientRegistry.registerBlockColors();
        FabricClientRegistry.registerParticles();
        ClientEventsFabric.register();
    }

}
