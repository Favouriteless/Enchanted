package net.favouriteless.enchanted.fabric.common;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.favouriteless.enchanted.api.curses.CurseManager;
import net.favouriteless.enchanted.common.CommonEvents;
import net.favouriteless.enchanted.common.effects.EffectEvents;
import net.favouriteless.enchanted.common.poppet.PoppetEvents;
import net.minecraft.server.level.ServerPlayer;
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
        ServerTickEvents.END_WORLD_TICK.register(CommonEvents::onLevelTick);

        EntitySleepEvents.ALLOW_SETTING_SPAWN.register((player, pos) -> {
            CommonEvents.onPlayerSleeping(player, pos);
            return true;
        });

        ServerPlayerEvents.COPY_FROM.register((original, player, alive) -> {
            if(player instanceof ServerPlayer sp)
                CurseManager.get().getCurses(sp.getUUID(), sp.serverLevel()).forEach(instance -> instance.getCurse().onInitialise(sp, instance.getStrength(), instance.getAge()));
        });

    }

}
