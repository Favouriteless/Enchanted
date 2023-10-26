package com.favouriteless.enchanted.client;

import com.favouriteless.enchanted.client.render.blockentity.*;
import com.favouriteless.enchanted.client.render.entity.*;
import com.favouriteless.enchanted.client.render.entity.ent.EntRenderer;
import com.favouriteless.enchanted.client.render.model.BroomstickModel;
import com.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import com.favouriteless.enchanted.client.render.model.armor.EarmuffsModel;
import com.favouriteless.enchanted.client.screens.*;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedMenuTypes;
import com.favouriteless.enchanted.platform.Services;
import com.favouriteless.enchanted.platform.services.IClientRegistryHelper;
import net.minecraft.client.gui.screens.MenuScreens;
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

        // MenuScreens
        MenuScreens.register(EnchantedMenuTypes.WITCH_OVEN.get(), WitchOvenScreen::new);
        MenuScreens.register(EnchantedMenuTypes.DISTILLERY.get(), DistilleryScreen::new);
        MenuScreens.register(EnchantedMenuTypes.ALTAR.get(), AltarScreen::new);
        MenuScreens.register(EnchantedMenuTypes.SPINNING_WHEEL.get(), SpinningWheelScreen::new);
        MenuScreens.register(EnchantedMenuTypes.POPPET_SHELF.get(), PoppetShelfScreen::new);

        // Layer definitions
        registry.register(ModelLayerLocations.BROOMSTICK, BroomstickModel::createLayerDefinition);
        registry.register(ModelLayerLocations.SPINNING_WHEEL, SpinningWheelRenderer::createLayerDefinition);
        registry.register(ModelLayerLocations.EARMUFFS, EarmuffsModel::createLayerDefinition);
    }

}
