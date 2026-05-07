package net.favouriteless.enchanted.client.init;

import net.favouriteless.enchanted.client.particles.*;
import net.favouriteless.enchanted.client.render.blockentity.KettleWaterRenderer;
import net.favouriteless.enchanted.client.render.blockentity.MortarRenderer;
import net.favouriteless.enchanted.client.render.blockentity.PoppetShelfRenderer;
import net.favouriteless.enchanted.client.render.blockentity.SpinningWheelRenderer;
import net.favouriteless.enchanted.client.render.entity.BroomstickRenderer;
import net.favouriteless.enchanted.client.render.entity.FamiliarCatRenderer;
import net.favouriteless.enchanted.client.render.entity.GeckolibEntityRenderer;
import net.favouriteless.enchanted.client.render.model.ModelLayerLocations;
import net.favouriteless.enchanted.client.render.model.entity.BroomstickModel;
import net.favouriteless.enchanted.client.screens.*;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.blocks.entity.EBlockEntityTypes;
import net.favouriteless.enchanted.common.init.*;
import net.favouriteless.enchanted.common.items.component.EDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientRegistry {

    public static void registerMenuScreens() {
        MenuScreens.register(EMenuTypes.WITCH_OVEN.get(), WitchOvenScreen::new);
        MenuScreens.register(EMenuTypes.DISTILLERY.get(), DistilleryScreen::new);
        MenuScreens.register(EMenuTypes.ALTAR.get(), AltarScreen::new);
        MenuScreens.register(EMenuTypes.SPINNING_WHEEL.get(), SpinningWheelScreen::new);
        MenuScreens.register(EMenuTypes.POPPET_SHELF.get(), PoppetShelfScreen::new);
    }

    public static void registerEntityRenderers() {
        EntityRenderers.register(EEntityTypes.MANDRAKE.get(), context -> new GeckolibEntityRenderer<>(context, EEntityTypes.MANDRAKE.get()));
        EntityRenderers.register(EEntityTypes.BROOMSTICK.get(), BroomstickRenderer::new);
        EntityRenderers.register(EEntityTypes.THROWABLE_BREW.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EEntityTypes.FAMILIAR_CAT.get(), FamiliarCatRenderer::new);
        EntityRenderers.register(EEntityTypes.VOODOO_ITEM.get(), ItemEntityRenderer::new);

        BlockEntityRenderers.register(EBlockEntityTypes.KETTLE.get(), KettleWaterRenderer::new);
        BlockEntityRenderers.register(EBlockEntityTypes.MORTAR.get(), MortarRenderer::new);
        BlockEntityRenderers.register(EBlockEntityTypes.SPINNING_WHEEL.get(), SpinningWheelRenderer::new);
        BlockEntityRenderers.register(EBlockEntityTypes.POPPET_SHELF.get(), PoppetShelfRenderer::new);
    }

    public static void registerParticleProviders() {
        ParticleEngine engine = Minecraft.getInstance().particleEngine;

        engine.register(EParticleTypes.BOILING.get(), BoilingParticle.Factory::new);
        engine.register(EParticleTypes.CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        engine.register(EParticleTypes.CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        engine.register(EParticleTypes.KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        engine.register(EParticleTypes.CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        engine.register(EParticleTypes.POPPET.get(), PoppetParticle.Factory::new);
        engine.register(EParticleTypes.IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        engine.register(EParticleTypes.IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        engine.register(EParticleTypes.TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        engine.register(EParticleTypes.BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        engine.register(EParticleTypes.SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        engine.register(EParticleTypes.SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        engine.register(EParticleTypes.CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        engine.register(EParticleTypes.BLIGHT_SEED.get(), BlightSeedParticle.Factory::new);
        engine.register(EParticleTypes.BLIGHT.get(), RepellingParticle.Factory::new);
        engine.register(EParticleTypes.REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        engine.register(EParticleTypes.REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
        engine.register(EParticleTypes.FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
        engine.register(EParticleTypes.FERTILITY.get(), RepellingParticle.Factory::new);
        engine.register(EParticleTypes.PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
        engine.register(EParticleTypes.PROTECTION.get(), ProtectionParticle.Factory::new);
        engine.register(EParticleTypes.BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
        engine.register(EParticleTypes.BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);
    }

    public static void registerLayerDefinitions(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
        consumer.accept(ModelLayerLocations.BROOMSTICK, BroomstickModel::createLayerDefinition);
        consumer.accept(ModelLayerLocations.SPINNING_WHEEL, SpinningWheelRenderer::createLayerDefinition);
    }

    public static void registerItemModelPredicates() {
        ItemProperties.register(EItems.CIRCLE_TALISMAN.get(), Enchanted.id("small"), (stack, level, entity, seed) -> talismanTexturePred(stack, Enchanted.id("small_circle")));
        ItemProperties.register(EItems.CIRCLE_TALISMAN.get(), Enchanted.id("medium"), (stack, level, entity, seed) -> talismanTexturePred(stack, Enchanted.id("medium_circle")));
        ItemProperties.register(EItems.CIRCLE_TALISMAN.get(), Enchanted.id("large"), (stack, level, entity, seed) -> talismanTexturePred(stack, Enchanted.id("large_circle")));
    }

    public static void registerBlockColors(BiConsumer<BlockColor, Block> consumer) {
        consumer.accept((a, b, c, d) -> 0xF0F0F0, EBlocks.RITUAL_CHALK.get());
        consumer.accept((a, b, c, d) -> 0x801818, EBlocks.NETHER_CHALK.get());
        consumer.accept((a, b, c, d) -> 0x4F2F78, EBlocks.OTHERWHERE_CHALK.get());
    }

    private static float talismanTexturePred(ItemStack stack, ResourceLocation id) {
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
