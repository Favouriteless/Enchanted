package net.favouriteless.enchanted.fabric.client;

import net.favouriteless.enchanted.client.ClientEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ClientEventsFabric {

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> ClientEvents.clientTickPost());
        ItemTooltipCallback.EVENT.register((item, context, flags, lines) -> ClientEvents.onItemTooltip(item, lines, flags));
        HudRenderCallback.EVENT.register((poseStack, partialTicks) -> ClientEvents.onRenderGui(poseStack, partialTicks.getGameTimeDeltaPartialTick(true)));
    }

}
