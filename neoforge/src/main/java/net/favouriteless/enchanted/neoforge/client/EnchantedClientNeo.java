package net.favouriteless.enchanted.neoforge.client;

import net.favouriteless.enchanted.client.ClientConfig;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.client.init.ClientRegistry;
import net.favouriteless.enchanted.client.init.EShaders;
import net.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.io.IOException;

@Mod(value = Enchanted.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Enchanted.MOD_ID, value = Dist.CLIENT)
public class EnchantedClientNeo {

    public EnchantedClientNeo(IEventBus bus, ModContainer container) {
        EnchantedClient.init();
        container.registerConfig(Type.CLIENT, ClientConfig.SPEC, "enchanted-client.toml");
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ClientRegistry.registerItemModelPredicates();
    }

    @SubscribeEvent
    public static void registerMenuScreens(final RegisterMenuScreensEvent event) {
        ClientRegistry.registerMenuScreens();
    }

    @SubscribeEvent
    public static void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        ClientRegistry.registerBlockColors(event::register);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        ClientRegistry.registerEntityRenderers();
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientRegistry.registerLayerDefinitions(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        ClientRegistry.registerParticleProviders();
    }

    @SubscribeEvent
    public static void registerClientExtensions(final RegisterClientExtensionsEvent event) {
        event.registerItem(
                new IClientItemExtensions() {
                    private BlockEntityWithoutLevelRenderer renderer;

                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        if (renderer == null) {
                            renderer = new SpinningWheelItemRenderer();
                        }

                        return renderer;
                    }
                }, EItems.SPINNING_WHEEL.get()
        );
    }

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent event) {
        EShaders.load((name, format, callback) -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), Enchanted.id(name), format), callback);
            } catch (IOException e) {
                Enchanted.LOG.error("Failed to load ShaderInstance: {}", name);
            }
        });
    }

}
