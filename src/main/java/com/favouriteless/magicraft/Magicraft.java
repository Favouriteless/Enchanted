package com.favouriteless.magicraft;

import com.favouriteless.magicraft.init.*;
import com.favouriteless.magicraft.tileentity.WitchOvenTileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Magicraft.MOD_ID)
public class Magicraft
{

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "magicraft";
    public static Magicraft instance;

    public Magicraft() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MagicraftRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        MagicraftRecipeTypes.init();
        MagicraftEntities.ENTITY_TYPES.register(modEventBus);
        MagicraftBlocks.BLOCKS.register(modEventBus);
        MagicraftItems.ITEMS.register(modEventBus);
        MagicraftTileEntities.TILE_ENTITY_TYPES.register(modEventBus);
        MagicraftContainerTypes.CONTAINER_TYPES.register(modEventBus);

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        WitchOvenTileEntity.init();
        MagicraftRituals.init();
    }
}
