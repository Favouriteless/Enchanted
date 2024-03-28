package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.client.particles.*;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticleTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import com.favouriteless.enchanted.platform.services.ForgeClientRegistryHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map.Entry;
import java.util.function.Supplier;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD, value=Dist.CLIENT)
public class ClientSetupEventsForge {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		EnchantedClient.init();

		event.enqueueWork(() -> {
			ItemProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), Enchanted.location("small"), (stack, world, living, seed) -> stack.hasTag() ? stack.getTag().getByte("small") * 0.3F : 0F);
			ItemProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), Enchanted.location("medium"), (stack, world, living, seed) -> stack.hasTag() ? stack.getTag().getByte("medium") * 0.3F : 0F);
			ItemProperties.register(EnchantedItems.CIRCLE_TALISMAN.get(), Enchanted.location("large"), (stack, world, living, seed) -> stack.hasTag() ? stack.getTag().getByte("large") * 0.3F : 0F);
		});
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((a, b, c, d) -> 0xF0F0F0, EnchantedBlocks.CHALK_WHITE.get());
		event.register((a, b, c, d) -> 0x801818, EnchantedBlocks.CHALK_RED.get());
		event.register((a, b, c, d) -> 0x4F2F78, EnchantedBlocks.CHALK_PURPLE.get());
	}

	@SubscribeEvent
	public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
		for(KeyMapping mapping : ForgeClientRegistryHelper.KEY_MAPPINGS)
			event.register(mapping);
	}

	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		ClientRegistry.registerEntityRenderers();
	}

	@SubscribeEvent
	public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ClientRegistry.registerLayerDefinitions();
		for(Pair<ModelLayerLocation, Supplier<LayerDefinition>> pair : ForgeClientRegistryHelper.LAYER_DEFINITIONS)
			event.registerLayerDefinition(pair.getKey(), pair.getValue());
	}

}
