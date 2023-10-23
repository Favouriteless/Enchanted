package com.favouriteless.enchanted.common.poppet;

import com.favouriteless.enchanted.Enchanted;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class PoppetEventsForge {

    @SubscribeEvent
    public static void onPlayerItemBreak(PlayerDestroyItemEvent event) {
        PoppetEvents.onPlayerItemBreak(event.getEntity(), event.getOriginal(), event.getHand());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if(PoppetEvents.onLivingEntityHurt(event.getEntity(), event.getAmount(), event.getSource()))
            event.setCanceled(true);
    }

}
