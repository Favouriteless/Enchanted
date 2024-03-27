package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.*;
import com.favouriteless.enchanted.client.render.blockentity.item.SpinningWheelItemRenderer;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.world.inventory.InventoryMenu;

public class FabricClientRegistry {

    public static void registerAll() {
        registerItemRenderers();
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

        registry.register(EnchantedParticleTypes.BOILING.get(), BoilingParticle.Factory::new);
        registry.register(EnchantedParticleTypes.CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        registry.register(EnchantedParticleTypes.CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        registry.register(EnchantedParticleTypes.KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        registry.register(EnchantedParticleTypes.CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        registry.register(EnchantedParticleTypes.POPPET.get(), PoppetParticle.Factory::new);
        registry.register(EnchantedParticleTypes.IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        registry.register(EnchantedParticleTypes.IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        registry.register(EnchantedParticleTypes.CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.CURSE_BLIGHT_SEED.get(), CurseBlightSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.CURSE_BLIGHT.get(), RepellingParticle.Factory::new);
        registry.register(EnchantedParticleTypes.REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
        registry.register(EnchantedParticleTypes.FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.FERTILITY.get(), RepellingParticle.Factory::new);
        registry.register(EnchantedParticleTypes.PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.PROTECTION.get(), ProtectionParticle.Factory::new);
        registry.register(EnchantedParticleTypes.BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
        registry.register(EnchantedParticleTypes.BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);

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
