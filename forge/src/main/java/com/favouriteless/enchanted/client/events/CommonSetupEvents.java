package com.favouriteless.enchanted.client.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.platform.ForgeCommonRegistryHelper;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class CommonSetupEvents {

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        for (SimpleJsonResourceReloadListener loader : ForgeCommonRegistryHelper.DATA_LOADERS.getLoaders())
            event.addListener(loader);
    }

}
