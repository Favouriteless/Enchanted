package com.favouriteless.enchanted;

import com.favouriteless.enchanted.platform.RegistryHandlerImpl;
import net.fabricmc.api.ModInitializer;

public class EnchantedFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        new RegistryHandlerImpl();
        Enchanted.init();
        Enchanted.loadRegistries();
    }

}
