package net.favouriteless.enchanted.neoforge.client;

import com.mojang.datafixers.util.Pair;
import net.favouriteless.enchanted.client.ClientConfig;
import net.favouriteless.enchanted.client.ClientRegistry;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.client.particles.*;
import net.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.platform.services.NeoClientRegistryHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.io.IOException;
import java.util.function.Supplier;

@Mod(value = Enchanted.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Enchanted.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class EnchantedClientNeo {

    public EnchantedClientNeo(IEventBus bus, ModContainer container) {
        container.registerConfig(Type.CLIENT, ClientConfig.SPEC, "enchanted-client.toml");
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        EnchantedClient.init();

        event.enqueueWork(ClientRegistry::registerItemModelPredicates);
    }

    @SubscribeEvent
    public static void registerMenuScreens(final RegisterMenuScreensEvent event) {
        ClientRegistry.registerMenuScreens();
        NeoClientRegistryHelper.MENU_FACTORY_REGISTERABLES.forEach(r -> r.register(event));
        NeoClientRegistryHelper.MENU_FACTORY_REGISTERABLES.clear();
    }

    @SubscribeEvent
    public static void registerBlockColors(final RegisterColorHandlersEvent.Block event) {
        event.register((a, b, c, d) -> 0xF0F0F0, EBlocks.RITUAL_CHALK.get());
        event.register((a, b, c, d) -> 0x801818, EBlocks.NETHER_CHALK.get());
        event.register((a, b, c, d) -> 0x4F2F78, EBlocks.OTHERWHERE_CHALK.get());
    }

    @SubscribeEvent
    public static void registerKeybinds(final RegisterKeyMappingsEvent event) {
        for(KeyMapping mapping : NeoClientRegistryHelper.KEY_MAPPINGS)
            event.register(mapping);
        NeoClientRegistryHelper.KEY_MAPPINGS.clear();
    }

    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        ClientRegistry.registerEntityRenderers();
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientRegistry.registerLayerDefinitions();
        for(Pair<ModelLayerLocation, Supplier<LayerDefinition>> pair : NeoClientRegistryHelper.LAYER_DEFINITIONS)
            event.registerLayerDefinition(pair.getFirst(), pair.getSecond());
        NeoClientRegistryHelper.LAYER_DEFINITIONS.clear();
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(EParticleTypes.BOILING.get(), BoilingParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.POPPET.get(), PoppetParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.BLIGHT_SEED.get(), BlightSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.BLIGHT.get(), RepellingParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.FERTILITY.get(), RepellingParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.PROTECTION.get(), ProtectionParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
        event.registerSpriteSet(EParticleTypes.BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(final RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            private SpinningWheelItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(renderer == null)
                    renderer = new SpinningWheelItemRenderer();

                return renderer;
            }
        }, EItems.SPINNING_WHEEL.get());
    }

    @SubscribeEvent
    public static void registerShaders(final RegisterShadersEvent event) {
        NeoClientRegistryHelper.SHADER_INSTANCES.forEach(r -> {
            try {
                event.registerShader(
                        new ShaderInstance(event.getResourceProvider(), Enchanted.id(r.name()), r.vertexFormat()),
                        r.loadCallback()
                );
            } catch(IOException e) {
                Enchanted.LOG.error("Failed to load ShaderInstance: {}", r.name());
            }
        });
        NeoClientRegistryHelper.SHADER_INSTANCES.clear();
    }

}
