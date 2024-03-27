package com.favouriteless.enchanted.common;

import com.favouriteless.enchanted.common.effects.EffectEvents;
import com.favouriteless.enchanted.common.poppet.PoppetEvents;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonEventsFabric {

    public static void playerDestroyItemEvent(Player player, @NotNull ItemStack item, @Nullable InteractionHand hand) {
        PoppetEvents.onPlayerItemBreak(player, item, hand);
    }

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> !PoppetEvents.onLivingEntityHurt(entity, amount, source)));
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(((entity, source, amount) -> !EffectEvents.onLivingHurt(entity, source, amount)));
        ServerTickEvents.START_WORLD_TICK.register(CommonEvents::onLevelTick);


        EntitySleepEvents.START_SLEEPING.register((entity, pos) -> {
            if(entity instanceof Player player)
                CommonEvents.onPlayerSleeping(player, pos);
        });
    }

}
