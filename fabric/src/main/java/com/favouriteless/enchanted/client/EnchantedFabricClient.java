package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.client.render.Renderers;
import net.fabricmc.api.ClientModInitializer;

public class EnchantedFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Renderers.register();
        FabricClientRegistry.registerBlockColors();
        FabricClientRegistry.registerParticles();
    }

}
