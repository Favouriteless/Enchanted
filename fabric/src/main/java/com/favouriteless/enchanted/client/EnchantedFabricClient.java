package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.client.screens.*;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenus;
import com.favouriteless.enchanted.platform.ClientRegistryHandlerImpl;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class EnchantedFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new ClientRegistryHandlerImpl();
        EnchantedClient.init();
        FabricClientRegistry.registerBlockColors();
        FabricClientRegistry.registerParticles();
        registerMenuScreens();
    }

    public static void registerMenuScreens() {
        MenuScreens.register(EnchantedMenus.WITCH_OVEN.get(), WitchOvenScreen::new);
        MenuScreens.register(EnchantedMenus.DISTILLERY.get(), DistilleryScreen::new);
        MenuScreens.register(EnchantedMenus.ALTAR.get(), AltarScreen::new);
        MenuScreens.register(EnchantedMenus.SPINNING_WHEEL.get(), SpinningWheelScreen::new);
        MenuScreens.register(EnchantedMenus.POPPET_SHELF.get(), PoppetShelfScreen::new);
    }

}
