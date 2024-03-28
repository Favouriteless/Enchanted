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
import net.minecraft.client.renderer.texture.TextureAtlas;
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
