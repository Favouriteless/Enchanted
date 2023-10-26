package com.favouriteless.enchanted.common;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.CommonEvents;
import com.favouriteless.enchanted.common.poppet.PoppetEvents;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=Enchanted.MOD_ID, bus=Bus.FORGE)
public class CommonEventsForge {

    @SubscribeEvent
    public static void onPlayerItemBreak(PlayerDestroyItemEvent event) {
        PoppetEvents.onPlayerItemBreak(event.getEntity(), event.getOriginal(), event.getHand());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if(PoppetEvents.onLivingEntityHurt(event.getEntity(), event.getAmount(), event.getSource()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
        CommonEvents.onPlayerSleeping(event.getEntity(), event.getPos());
    }

}
