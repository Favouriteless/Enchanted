package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.platform.services.FabricClientRegistryHelper;
import net.fabricmc.api.ClientModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class EnchantedFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new FabricClientRegistryHelper();
        EnchantedClient.init();
        FabricClientRegistry.registerAll();
        ClientEventsFabric.register();
        ModLoadingContext.registerConfig(Enchanted.MOD_ID, Type.CLIENT, ClientConfig.SPEC, "enchanted-client.toml");
    }

}
