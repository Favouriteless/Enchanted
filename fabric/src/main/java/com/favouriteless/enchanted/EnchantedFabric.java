package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.CommonEventsFabric;
import com.favouriteless.enchanted.common.entities.Ent;
import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.favouriteless.enchanted.common.entities.Mandrake;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

public class EnchantedFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Enchanted.init();
        registerEntityAttributes();
        CommonEventsFabric.register();
        EnchantedItems.registerCompostables();
        ModLoadingContext.registerConfig(Enchanted.MOD_ID, Type.COMMON, CommonConfig.SPEC, "enchanted-common.toml");
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(EnchantedEntityTypes.MANDRAKE.get(), Mandrake.createAttributes());
        FabricDefaultAttributeRegistry.register(EnchantedEntityTypes.ENT.get(), Ent.createAttributes());
        FabricDefaultAttributeRegistry.register(EnchantedEntityTypes.FAMILIAR_CAT.get(), FamiliarCat.createCatAttributes());
    }

}
