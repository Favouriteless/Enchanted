package net.favouriteless.enchanted.fabric.client;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.favouriteless.enchanted.client.ClientRegistry;
import net.favouriteless.enchanted.client.particles.*;
import net.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.init.EParticleTypes;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.favouriteless.enchanted.platform.services.FabricClientRegistryHelper;
import net.favouriteless.enchanted.platform.services.FabricNetworkHelper;
import net.favouriteless.enchanted.platform.services.FabricNetworkHelper.ClientPayloadRegisterable;
import net.minecraft.client.renderer.RenderType;

import java.io.IOException;

public class FabricClientRegistry {

    public static void registerAll() {
        registerItemRenderers();
        registerBlockColors();
        registerBlockRenderTypes();
        registerParticleFactories();
        ClientRegistry.registerItemModelPredicates();
        ClientRegistry.registerLayerDefinitions();
        ClientRegistry.registerMenuScreens();
        ClientRegistry.registerEntityRenderers();

        FabricNetworkHelper.clientHandlers.forEach(ClientPayloadRegisterable::register);

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            FabricClientRegistryHelper.SHADER_INSTANCE_REGISTERABLES.forEach(r -> {
                try {
                    context.register(
                            Enchanted.id(r.name()),
                            r.vertexFormat(),
                            r.loadCallback()
                    );
                } catch(IOException e) {
                    Enchanted.LOG.error("Failed to load ShaderInstance: {}", r.name());
                }
            });
        });
    }

    private static void registerItemRenderers() {
        BuiltinItemRendererRegistry.INSTANCE.register(EItems.SPINNING_WHEEL.get(), SpinningWheelItemRenderer.getInstance()::renderByItem);
    }

    private static void registerBlockColors() {
        ColorProviderRegistry.BLOCK.register((a, b, c, d) -> 0xF0F0F0, EBlocks.RITUAL_CHALK.get());
        ColorProviderRegistry.BLOCK.register((a, b, c, d) -> 0x801818, EBlocks.NETHER_CHALK.get());
        ColorProviderRegistry.BLOCK.register((a, b, c, d) -> 0x4F2F78, EBlocks.OTHERWHERE_CHALK.get());
    }

    private static void registerBlockRenderTypes() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.GOLDEN_CHALK.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.RITUAL_CHALK.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.NETHER_CHALK.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.OTHERWHERE_CHALK.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.ROWAN_SAPLING.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.HAWTHORN_SAPLING.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.ALDER_SAPLING.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.BELLADONNA.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.SNOWBELL.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.WATER_ARTICHOKE.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.MANDRAKE.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.GARLIC.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.WOLFSBANE.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.GLINT_WEED.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.EMBER_MOSS.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.SPANISH_MOSS.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.BLOOD_POPPY.get());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), EBlocks.WITCH_CAULDRON.get());
    }

    private static void registerParticleFactories() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(EParticleTypes.BOILING.get(), BoilingParticle.Factory::new);
        registry.register(EParticleTypes.CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        registry.register(EParticleTypes.CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        registry.register(EParticleTypes.KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        registry.register(EParticleTypes.CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        registry.register(EParticleTypes.POPPET.get(), PoppetParticle.Factory::new);
        registry.register(EParticleTypes.IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        registry.register(EParticleTypes.IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        registry.register(EParticleTypes.TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        registry.register(EParticleTypes.BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        registry.register(EParticleTypes.SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        registry.register(EParticleTypes.SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        registry.register(EParticleTypes.CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        registry.register(EParticleTypes.BLIGHT_SEED.get(), BlightSeedParticle.Factory::new);
        registry.register(EParticleTypes.BLIGHT.get(), RepellingParticle.Factory::new);
        registry.register(EParticleTypes.REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        registry.register(EParticleTypes.REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
        registry.register(EParticleTypes.FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
        registry.register(EParticleTypes.FERTILITY.get(), RepellingParticle.Factory::new);
        registry.register(EParticleTypes.PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
        registry.register(EParticleTypes.PROTECTION.get(), ProtectionParticle.Factory::new);
        registry.register(EParticleTypes.BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
        registry.register(EParticleTypes.BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);
    }

}
