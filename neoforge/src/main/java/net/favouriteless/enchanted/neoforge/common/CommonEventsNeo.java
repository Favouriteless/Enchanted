package net.favouriteless.enchanted.neoforge.common;

import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.common.CommonEvents;
import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.common.effects.EffectEvents;
import net.favouriteless.enchanted.common.poppet.PoppetEvents;
import net.favouriteless.enchanted.platform.services.NeoCommonRegistryHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = Enchanted.MOD_ID)
public class CommonEventsNeo {

    @SubscribeEvent
    public static void onPlayerItemBreak(PlayerDestroyItemEvent event) {
        PoppetEvents.onPlayerItemBreak(event.getEntity(), event.getOriginal(), event.getHand());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onArmourHurt(ArmorHurtEvent event) {
        event.getArmorMap().forEach((slot, entry) -> {
            if(PoppetEvents.onArmourHurt(event.getEntity(), slot, entry.armorItemStack, entry.newDamage)) {
                event.setCanceled(true);
            }
        }
        );
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        if(EffectEvents.onLivingHurt(event.getEntity(), event.getSource(), event.getOriginalDamage())) {
            event.setNewDamage(0);
            return;
        }
        if(PoppetEvents.onLivingEntityHurt(event.getEntity(), event.getOriginalDamage(), event.getSource()))
            event.setNewDamage(0);
    }

    @SubscribeEvent
    public static void onPlayerSleep(CanPlayerSleepEvent event) {
        CommonEvents.onPlayerSleeping(event.getEntity(), event.getPos());
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        CommonEvents.onLevelTick(event.getLevel());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        CommonEvents.onPlayerLoggedIn(event.getEntity());
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        for(SimpleJsonResourceReloadListener loader : NeoCommonRegistryHelper.dataLoaders)
            event.addListener(loader);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(event.getEntity() instanceof ServerPlayer player)
            CurseManager.get().getCurses(player.getUUID(), player.serverLevel()).forEach(instance -> instance.getCurse().onInitialise(player, instance.getStrength(), instance.getAge()));
    }

}
