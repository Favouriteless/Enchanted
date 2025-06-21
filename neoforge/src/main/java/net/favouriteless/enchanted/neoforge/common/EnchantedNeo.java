package net.favouriteless.enchanted.neoforge.common;

import net.favouriteless.enchanted.common.CommonConfig;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.ServerConfig;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.favouriteless.enchanted.common.entities.FamiliarCat;
import net.favouriteless.enchanted.common.entities.Mandrake;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.platform.services.NeoCommonRegistryHelper;
import net.favouriteless.enchanted.platform.services.NeoCommonRegistryHelper.DataRegistryRegisterable;
import net.favouriteless.enchanted.platform.services.NeoNetworkHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@Mod(Enchanted.MOD_ID)
@EventBusSubscriber(modid = Enchanted.MOD_ID, bus = Bus.MOD)
public class EnchantedNeo {
    
    public EnchantedNeo(IEventBus bus, ModContainer container) {
        Enchanted.init();

        container.registerConfig(Type.COMMON, CommonConfig.SPEC, "enchanted-common.toml");
        container.registerConfig(Type.SERVER, ServerConfig.SPEC, "enchanted-server.toml");

        NeoCommonRegistryHelper.getRegistryMap().register(bus);
        bus.addListener(NeoNetworkHelper::registerPayloads);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent events) {
        EItems.registerCompostables();
        EBlocks.registerFlammables();
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EEntityTypes.MANDRAKE.get(), Mandrake.createAttributes());
        event.put(EEntityTypes.FAMILIAR_CAT.get(), FamiliarCat.createCatAttributes());
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        for(DataRegistryRegisterable<?> registerable : NeoCommonRegistryHelper.dataRegistryRegisterables) {
            registerable.register(event);
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntityTypes.WITCH_OVEN.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntityTypes.DISTILLERY.get(), SidedInvWrapper::new);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, EBlockEntityTypes.SPINNING_WHEEL.get(), SidedInvWrapper::new);
    }

}