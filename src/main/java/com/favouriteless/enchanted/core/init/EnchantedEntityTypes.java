package com.favouriteless.enchanted.core.init;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.entities.mandrake.MandrakeEntity;
import com.favouriteless.enchanted.common.entities.mandrake.MandrakeRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantedEntityTypes {

    public static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EnchantedEntityTypes.MANDRAKE.get(), MandrakeRenderer::new);
    }

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Enchanted.MOD_ID);

    public static final RegistryObject<EntityType<MandrakeEntity>> MANDRAKE = ENTITY_TYPES.register("mandrake", () -> EntityType.Builder.<MandrakeEntity>of(MandrakeEntity::new, EntityClassification.MONSTER)
            .sized(0.4F, 0.7F)
            .build(new ResourceLocation(Enchanted.MOD_ID, "mandrake").toString()));

}
