package com.favouriteless.magicraft.init;

import com.favouriteless.magicraft.Magicraft;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MagicraftEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Magicraft.MOD_ID);

    public static void initRender(FMLClientSetupEvent event) {

    }

}
