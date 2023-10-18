package com.favouriteless.enchanted.client.events;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.*;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class ClientSetupEvents {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), Enchanted.location("small"), (stack, world, living, seed) -> stack.hasTag() ? stack.getTag().getByte("small") * 0.3F : 0F);
			ItemProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), Enchanted.location("medium"), (stack, world, living, seed) -> stack.hasTag() ? stack.getTag().getByte("medium") * 0.3F : 0F);
			ItemProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), Enchanted.location("large"), (stack, world, living, seed) -> stack.hasTag() ? stack.getTag().getByte("large") * 0.3F : 0F);
		});
	}

	@SubscribeEvent
	public static void onRegisterParticleFactories(RegisterParticleProvidersEvent event) {
		event.register(EnchantedParticles.BOILING.get(), BoilingParticle.Factory::new);
		event.register(EnchantedParticles.CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
		event.register(EnchantedParticles.CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
		event.register(EnchantedParticles.KETTLE_COOK.get(), KettleCookParticle.Factory::new);
		event.register(EnchantedParticles.CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
		event.register(EnchantedParticles.POPPET.get(), PoppetParticle.Factory::new);
		event.register(EnchantedParticles.IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
		event.register(EnchantedParticles.IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
		event.register(EnchantedParticles.TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
		event.register(EnchantedParticles.BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
		event.register(EnchantedParticles.SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
		event.register(EnchantedParticles.SKY_WRATH.get(), SkyWrathParticle.Factory::new);
		event.register(EnchantedParticles.CURSE_SEED.get(), CurseSeedParticle.Factory::new);
		event.register(EnchantedParticles.CURSE_BLIGHT_SEED.get(), CurseBlightSeedParticle.Factory::new);
		event.register(EnchantedParticles.CURSE_BLIGHT.get(), RepellingParticle.Factory::new);
		event.register(EnchantedParticles.REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
		event.register(EnchantedParticles.REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
		event.register(EnchantedParticles.FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
		event.register(EnchantedParticles.FERTILITY.get(), RepellingParticle.Factory::new);
		event.register(EnchantedParticles.PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
		event.register(EnchantedParticles.PROTECTION.get(), ProtectionParticle.Factory::new);
		event.register(EnchantedParticles.BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
		event.register(EnchantedParticles.BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);
	}

}
