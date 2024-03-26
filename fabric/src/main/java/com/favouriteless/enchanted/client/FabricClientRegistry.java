package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.*;
import com.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.HashMap;
import java.util.Map;

public class FabricClientRegistry {

    public static void registerAll() {
        registerItemRenderers();
        registerArmorRenderers();
        registerBlockColors();
        registerParticles();
    }

    private static void registerItemRenderers() {
        BuiltinItemRendererRegistry.INSTANCE.register(EnchantedItems.SPINNING_WHEEL.get(), SpinningWheelItemRenderer.getInstance()::renderByItem);
    }

    private static void registerBlockColors() {
        ColorProviderRegistry.BLOCK.register((a, b, c, d) -> 0xF0F0F0, EnchantedBlocks.CHALK_WHITE.get());
        ColorProviderRegistry.BLOCK.register((a, b, c, d) -> 0x801818, EnchantedBlocks.CHALK_RED.get());
        ColorProviderRegistry.BLOCK.register((a, b, c, d) -> 0x4F2F78, EnchantedBlocks.CHALK_PURPLE.get());
    }

    private static void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();

        registry.register(EnchantedParticles.BOILING.get(), BoilingParticle.Factory::new);
        registry.register(EnchantedParticles.CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        registry.register(EnchantedParticles.CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        registry.register(EnchantedParticles.KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        registry.register(EnchantedParticles.CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        registry.register(EnchantedParticles.POPPET.get(), PoppetParticle.Factory::new);
        registry.register(EnchantedParticles.IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        registry.register(EnchantedParticles.IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        registry.register(EnchantedParticles.TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        registry.register(EnchantedParticles.BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        registry.register(EnchantedParticles.SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        registry.register(EnchantedParticles.SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        registry.register(EnchantedParticles.CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        registry.register(EnchantedParticles.CURSE_BLIGHT_SEED.get(), CurseBlightSeedParticle.Factory::new);
        registry.register(EnchantedParticles.CURSE_BLIGHT.get(), RepellingParticle.Factory::new);
        registry.register(EnchantedParticles.REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        registry.register(EnchantedParticles.REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
        registry.register(EnchantedParticles.FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
        registry.register(EnchantedParticles.FERTILITY.get(), RepellingParticle.Factory::new);
        registry.register(EnchantedParticles.PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
        registry.register(EnchantedParticles.PROTECTION.get(), ProtectionParticle.Factory::new);
        registry.register(EnchantedParticles.BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
        registry.register(EnchantedParticles.BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);

        ClientSpriteRegistryCallback.event(InventoryMenu.BLOCK_ATLAS).register(((atlas, event) -> {
            event.register(Enchanted.location("particle/bubble"));
            event.register(Enchanted.location("particle/bubble_pop_0"));
            event.register(Enchanted.location("particle/bubble_pop_1"));
            event.register(Enchanted.location("particle/bubble_pop_2"));
            event.register(Enchanted.location("particle/bubble_pop_3"));
            event.register(Enchanted.location("particle/bubble_pop_4"));
            event.register(Enchanted.location("particle/sky_wrath"));
            event.register(Enchanted.location("particle/static_flame"));
        }));
    }

}
