package com.favouriteless.enchanted.core.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.screens.DistilleryScreen;
import com.favouriteless.enchanted.client.screens.WitchOvenScreen;
import com.favouriteless.enchanted.core.init.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        EnchantedRecipeTypes.init();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.register(EnchantedContainerTypes.WITCH_OVEN.get(), WitchOvenScreen::new);
        ScreenManager.register(EnchantedContainerTypes.DISTILLERY.get(), DistilleryScreen::new);
        
        EnchantedBlocks.initRender();
    }

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((a, b, c, d) -> 0xF0F0F0, EnchantedBlocks.CHALK_WHITE.get());
        event.getBlockColors().register((a, b, c, d) -> 0x801818, EnchantedBlocks.CHALK_RED.get());
        event.getBlockColors().register((a, b, c, d) -> 0x4F2F78, EnchantedBlocks.CHALK_PURPLE.get());
    }
}
