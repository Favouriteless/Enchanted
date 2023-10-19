package com.favouriteless.enchanted;

import com.favouriteless.enchanted.common.entities.Ent;
import com.favouriteless.enchanted.common.entities.FamiliarCat;
import com.favouriteless.enchanted.common.entities.Mandrake;
import com.favouriteless.enchanted.common.init.registry.EnchantedEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Enchanted.MOD_ID)
@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.MOD)
public class EnchantedForge {
    
    public EnchantedForge() {
        Enchanted.init();
    }

    @SubscribeEvent
    public static void onRegistry(RegisterEvent event) {
        Enchanted.loadRegistries();
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EnchantedEntityTypes.MANDRAKE.get(), Mandrake.createAttributes());
        event.put(EnchantedEntityTypes.ENT.get(), Ent.createAttributes());
        event.put(EnchantedEntityTypes.FAMILIAR_CAT.get(), FamiliarCat.createCatAttributes());
    }

}