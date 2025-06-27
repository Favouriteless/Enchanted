package net.favouriteless.enchanted.fabric.client;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.favouriteless.enchanted.client.ClientConfig;
import net.favouriteless.enchanted.client.ClientRegistry;
import net.favouriteless.enchanted.client.EShaders;
import net.favouriteless.enchanted.client.EnchantedClient;
import net.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.platform.services.FabricNetworkHelper;
import net.favouriteless.enchanted.platform.services.FabricNetworkHelper.ClientPayloadRegisterable;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.fml.config.ModConfig.Type;

import java.io.IOException;

public class EnchantedClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EnchantedClient.init();
        ClientEventsFabric.register();

        ClientRegistry.registerItemModelPredicates();
        ClientRegistry.registerBlockColors(ColorProviderRegistry.BLOCK::register);
        ClientRegistry.registerLayerDefinitions((loc, def) -> EntityModelLayerRegistry.registerModelLayer(loc, def::get));
        ClientRegistry.registerMenuScreens();
        ClientRegistry.registerEntityRenderers();

        registerItemRenderers();
        registerBlockRenderTypes();

        FabricNetworkHelper.clientHandlers.forEach(ClientPayloadRegisterable::register);

        CoreShaderRegistrationCallback.EVENT.register(context -> EShaders.load((name, format, callback) -> {
            try {
                context.register(Enchanted.id(name), format, callback);
            } catch(IOException e) {
                Enchanted.LOG.error("Failed to load ShaderInstance: {}", name);
            }
        }));

        NeoForgeConfigRegistry.INSTANCE.register(Enchanted.MOD_ID, Type.CLIENT, ClientConfig.SPEC, "enchanted-client.toml");
    }

    private static void registerItemRenderers() {
        BuiltinItemRendererRegistry.INSTANCE.register(EItems.SPINNING_WHEEL.get(), SpinningWheelItemRenderer.getInstance()::renderByItem);
    }

    private static void registerBlockRenderTypes() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                EBlocks.GOLDEN_CHALK.get(),
                EBlocks.RITUAL_CHALK.get(),
                EBlocks.NETHER_CHALK.get(),
                EBlocks.OTHERWHERE_CHALK.get(),
                EBlocks.ROWAN_SAPLING.get(),
                EBlocks.HAWTHORN_SAPLING.get(),
                EBlocks.ALDER_SAPLING.get(),
                EBlocks.BELLADONNA.get(),
                EBlocks.SNOWBELL.get(),
                EBlocks.WATER_ARTICHOKE.get(),
                EBlocks.MANDRAKE.get(),
                EBlocks.GARLIC.get(),
                EBlocks.WOLFSBANE.get(),
                EBlocks.GLINT_WEED.get(),
                EBlocks.EMBER_MOSS.get(),
                EBlocks.SPANISH_MOSS.get(),
                EBlocks.BLOOD_POPPY.get(),
                EBlocks.WITCH_CAULDRON.get()
        );
    }

}
