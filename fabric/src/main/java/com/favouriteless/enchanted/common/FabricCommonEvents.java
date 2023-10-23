package com.favouriteless.enchanted.common;

import com.favouriteless.enchanted.common.poppet.PoppetEvents;
import com.favouriteless.enchanted.common.poppet.PoppetEventsFabric;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Event handlers for things which replicate Forge events are held in {@link FabricCommonEvents} due to how some events
 * have many points they're called from.
 */
public class FabricCommonEvents {

    public static void playerDestroyItemEvent(Player player, @NotNull ItemStack item, @Nullable InteractionHand hand) {
        PoppetEvents.onPlayerItemBreak(player, item, hand);
    }

    public static void register() {
        PoppetEventsFabric.register();
    }

}
