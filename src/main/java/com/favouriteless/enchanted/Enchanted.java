package com.favouriteless.enchanted;

import com.favouriteless.enchanted.core.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Enchanted.MOD_ID)
public class Enchanted
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "enchanted";
    public static Enchanted instance;

    public Enchanted() {
        registerAll();

        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerAll() {

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EnchantedRecipeTypes.init();
        EnchantedRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        EnchantedEntityTypes.ENTITY_TYPES.register(modEventBus);
        EnchantedBlocks.BLOCKS.register(modEventBus);
        EnchantedItems.ITEMS.register(modEventBus);
        EnchantedTileEntities.TILE_ENTITY_TYPES.register(modEventBus);
        EnchantedContainers.CONTAINER_TYPES.register(modEventBus);
    }


}
