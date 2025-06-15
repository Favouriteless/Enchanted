package net.favouriteless.enchanted.fabric.common;

import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.favouriteless.enchanted.common.CommonConfig;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.init.EBlocks;
import net.favouriteless.enchanted.common.init.EEntityTypes;
import net.favouriteless.enchanted.common.entities.FamiliarCat;
import net.favouriteless.enchanted.common.entities.Mandrake;
import net.favouriteless.enchanted.common.init.EItems;
import net.neoforged.fml.config.ModConfig.Type;

public class EnchantedFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Enchanted.init();
        registerEntityAttributes();
        CommonEventsFabric.register();
        EBlocks.registerFlammables();
        EItems.registerCompostables();
        registerStrippables();

        NeoForgeConfigRegistry.INSTANCE.register(Enchanted.MOD_ID, Type.COMMON, CommonConfig.SPEC, "enchanted-common.toml");
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(EEntityTypes.MANDRAKE.get(), Mandrake.createAttributes());
        FabricDefaultAttributeRegistry.register(EEntityTypes.FAMILIAR_CAT.get(), FamiliarCat.createCatAttributes());
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(EBlocks.ALDER_LOG.get(), EBlocks.STRIPPED_ALDER_LOG.get());
        StrippableBlockRegistry.register(EBlocks.HAWTHORN_LOG.get(), EBlocks.STRIPPED_HAWTHORN_LOG.get());
        StrippableBlockRegistry.register(EBlocks.ROWAN_LOG.get(), EBlocks.STRIPPED_ROWAN_LOG.get());
    }

}
