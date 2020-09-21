package com.favouriteless.magicraft.util;

import com.favouriteless.magicraft.Magicraft;
import com.favouriteless.magicraft.client.gui.*;
import com.favouriteless.magicraft.init.MagicraftBlocks;
import com.favouriteless.magicraft.init.MagicraftContainerTypes;
import com.favouriteless.magicraft.init.MagicraftEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid=Magicraft.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(MagicraftContainerTypes.WITCH_OVEN.get(), WitchOvenScreen::new);
        ScreenManager.registerFactory(MagicraftContainerTypes.DISTILLERY.get(), DistilleryScreen::new);

        MagicraftBlocks.initRender(event);
        MagicraftEntities.initRender(event);
    }

}
