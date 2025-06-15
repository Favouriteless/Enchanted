package net.favouriteless.enchanted.client;

import net.favouriteless.enchanted.client.render.blockentity.CauldronWaterRenderer;
import net.favouriteless.enchanted.client.render.blockentity.PoppetShelfRenderer;
import net.favouriteless.enchanted.client.render.blockentity.SpinningWheelRenderer;
import net.favouriteless.enchanted.client.render.entity.BroomstickRenderer;
import net.favouriteless.enchanted.client.render.entity.FamiliarCatRenderer;
import net.favouriteless.enchanted.client.render.entity.GeckolibEntityRenderer;
import net.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import net.favouriteless.enchanted.client.render.model.entity.BroomstickModel;
import net.favouriteless.enchanted.client.screens.*;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.favouriteless.enchanted.common.init.EItems;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.favouriteless.enchanted.common.init.EMenuTypes;
import net.favouriteless.enchanted.platform.ClientServices;
import net.favouriteless.enchanted.platform.services.IClientRegistryHelper;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class ClientRegistry {

    public static void registerMenuScreens() {
        IClientRegistryHelper registry = ClientServices.CLIENT_REGISTRY;

        registry.register(EMenuTypes.WITCH_OVEN.get(), WitchOvenScreen::new);
        registry.register(EMenuTypes.DISTILLERY.get(), DistilleryScreen::new);
        registry.register(EMenuTypes.ALTAR.get(), AltarScreen::new);
        registry.register(EMenuTypes.SPINNING_WHEEL.get(), SpinningWheelScreen::new);
        registry.register(EMenuTypes.POPPET_SHELF.get(), PoppetShelfScreen::new);
    }

    public static void registerEntityRenderers() {
        IClientRegistryHelper registry = ClientServices.CLIENT_REGISTRY;

        // Entity renderers
        registry.register(EEntityTypes.MANDRAKE.get(), context -> new GeckolibEntityRenderer<>(context, EEntityTypes.MANDRAKE.get()));
        registry.register(EEntityTypes.BROOMSTICK.get(), BroomstickRenderer::new);
        registry.register(EEntityTypes.THROWABLE_BREW.get(), ThrownItemRenderer::new);
        registry.register(EEntityTypes.FAMILIAR_CAT.get(), FamiliarCatRenderer::new);
        registry.register(EEntityTypes.VOODOO_ITEM.get(), ItemEntityRenderer::new);

        // Block entity renderers
        registry.register(EBlockEntityTypes.WITCH_CAULDRON.get(), context -> new CauldronWaterRenderer<>(12));
        registry.register(EBlockEntityTypes.KETTLE.get(), context -> new CauldronWaterRenderer<>(8));
        registry.register(EBlockEntityTypes.SPINNING_WHEEL.get(), SpinningWheelRenderer::new);
        registry.register(EBlockEntityTypes.POPPET_SHELF.get(), PoppetShelfRenderer::new);
    }

    public static void registerLayerDefinitions() {
        IClientRegistryHelper registry = ClientServices.CLIENT_REGISTRY;

        // Layer definitions
        registry.register(ModelLayerLocations.BROOMSTICK, BroomstickModel::createLayerDefinition);
        registry.register(ModelLayerLocations.SPINNING_WHEEL, SpinningWheelRenderer::createLayerDefinition);
    }

    public static void registerItemModelPredicates() {
        IClientRegistryHelper registry = ClientServices.CLIENT_REGISTRY;

        registry.register(EItems.CIRCLE_TALISMAN.get(), Enchanted.id("small"), (stack, level, entity, seed) -> talismanTexturePred(stack, Enchanted.id("small_circle")));
        registry.register(EItems.CIRCLE_TALISMAN.get(), Enchanted.id("medium"), (stack, level, entity, seed) -> talismanTexturePred(stack, Enchanted.id("medium_circle")));
        registry.register(EItems.CIRCLE_TALISMAN.get(), Enchanted.id("large"), (stack, level, entity, seed) -> talismanTexturePred(stack, Enchanted.id("large_circle")));
    }

    protected static float talismanTexturePred(ItemStack stack, ResourceLocation id) {
        Map<ResourceLocation, Block> shapes = stack.get(EDataComponents.CIRCLE_MAGIC_SHAPE_MAP.get());
        Block block = shapes.get(id);

        if(block == EBlocks.RITUAL_CHALK.get())
            return 0.3F;
        if(block == EBlocks.NETHER_CHALK.get())
            return 0.6F;
        if(block == EBlocks.OTHERWHERE_CHALK.get())
            return 0.9F;
        return 0.0F;
    }

}
