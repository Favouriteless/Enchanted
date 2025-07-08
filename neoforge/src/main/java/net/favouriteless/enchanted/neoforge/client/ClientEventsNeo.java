package net.favouriteless.enchanted.neoforge.client;

import net.favouriteless.enchanted.client.ClientEvents;
import net.favouriteless.enchanted.common.Enchanted;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = Enchanted.MOD_ID, value = Dist.CLIENT)
public class ClientEventsNeo {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientEvents.clientTickPost();
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ClientEvents.onItemTooltip(event.getItemStack(), event.getToolTip(), event.getFlags());
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        ClientEvents.onRenderGui(event.getGuiGraphics(), event.getPartialTick().getRealtimeDeltaTicks());
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        ClientEvents.playSound(event.getSound());
    }

}
