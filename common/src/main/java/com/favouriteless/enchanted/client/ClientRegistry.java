package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.client.render.blockentity.CauldronWaterRenderer;
import com.favouriteless.enchanted.client.render.blockentity.PoppetShelfRenderer;
import com.favouriteless.enchanted.client.render.blockentity.SpinningWheelRenderer;
import com.favouriteless.enchanted.client.render.entity.BroomstickRenderer;
import com.favouriteless.enchanted.client.render.entity.FamiliarCatRenderer;
import com.favouriteless.enchanted.client.render.entity.SimpleAnimatedGeoRenderer;
import com.favouriteless.enchanted.client.render.entity.ent.EntRenderer;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.platform.Services;
import com.favouriteless.enchanted.platform.services.IClientRegistryHelper;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class ClientRegistry {

    public static void register() {
        IClientRegistryHelper registry = Services.CLIENT_REGISTRY;

        // Entity renderers
        registry.register(EnchantedEntityTypes.MANDRAKE.get(), context -> new SimpleAnimatedGeoRenderer<>(context, "mandrake"));
        registry.register(EnchantedEntityTypes.ENT.get(), EntRenderer::new);
        registry.register(EnchantedEntityTypes.BROOMSTICK.get(), BroomstickRenderer::new);
        registry.register(EnchantedEntityTypes.THROWABLE_BREW.get(), ThrownItemRenderer::new);
        registry.register(EnchantedEntityTypes.FAMILIAR_CAT.get(), FamiliarCatRenderer::new);

        // Block entity renderers
        registry.register(EnchantedBlockEntityTypes.WITCH_CAULDRON.get(), context -> new CauldronWaterRenderer<>(10));
        registry.register(EnchantedBlockEntityTypes.KETTLE.get(), context -> new CauldronWaterRenderer<>(8));
        registry.register(EnchantedBlockEntityTypes.SPINNING_WHEEL.get(), SpinningWheelRenderer::new);
        registry.register(EnchantedBlockEntityTypes.POPPET_SHELF.get(), PoppetShelfRenderer::new);
    }

}
