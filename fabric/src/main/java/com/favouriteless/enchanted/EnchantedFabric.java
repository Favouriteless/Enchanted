package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.entities.Ent;
import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.favouriteless.enchanted.common.entities.Mandrake;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import com.favouriteless.enchanted.platform.RegistryHandlerImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class EnchantedFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        new RegistryHandlerImpl();
        Enchanted.init();
        Enchanted.loadRegistries();
        registerEntityAttributes();
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(EnchantedEntityTypes.MANDRAKE.get(), Mandrake.createAttributes());
        FabricDefaultAttributeRegistry.register(EnchantedEntityTypes.ENT.get(), Ent.createAttributes());
        FabricDefaultAttributeRegistry.register(EnchantedEntityTypes.FAMILIAR_CAT.get(), FamiliarCat.createCatAttributes());
    }

}
