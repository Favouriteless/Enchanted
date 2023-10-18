package com.favouriteless.enchanted.client.render;

import com.favouriteless.enchanted.client.render.blockentity.CauldronWaterRenderer;
import com.favouriteless.enchanted.client.render.blockentity.PoppetShelfRenderer;
import com.favouriteless.enchanted.client.render.blockentity.SpinningWheelRenderer;
import com.favouriteless.enchanted.client.render.entity.BroomstickRenderer;
import com.favouriteless.enchanted.client.render.entity.FamiliarCatRenderer;
import com.favouriteless.enchanted.client.render.entity.SimpleAnimatedGeoRenderer;
import com.favouriteless.enchanted.client.render.entity.ent.EntRenderer;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.platform.RegistryHandler;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

public class Renderers {

    public static void register() {
        // Entity renderers
        RegistryHandler.register(EnchantedEntityTypes.MANDRAKE.get(), context -> new SimpleAnimatedGeoRenderer<>(context, "mandrake"));
        RegistryHandler.register(EnchantedEntityTypes.ENT.get(), EntRenderer::new);
        RegistryHandler.register(EnchantedEntityTypes.BROOMSTICK.get(), BroomstickRenderer::new);
        RegistryHandler.register(EnchantedEntityTypes.THROWABLE_BREW.get(), ThrownItemRenderer::new);
        RegistryHandler.register(EnchantedEntityTypes.FAMILIAR_CAT.get(), FamiliarCatRenderer::new);

        // Block entity renderers
        RegistryHandler.register(EnchantedBlockEntityTypes.WITCH_CAULDRON.get(), dispatcher -> new CauldronWaterRenderer<>(10));
        RegistryHandler.register(EnchantedBlockEntityTypes.KETTLE.get(), dispatcher -> new CauldronWaterRenderer<>(8));
        RegistryHandler.register(EnchantedBlockEntityTypes.SPINNING_WHEEL.get(), SpinningWheelRenderer::new);
        RegistryHandler.register(EnchantedBlockEntityTypes.POPPET_SHELF.get(), PoppetShelfRenderer::new);
    }

}
