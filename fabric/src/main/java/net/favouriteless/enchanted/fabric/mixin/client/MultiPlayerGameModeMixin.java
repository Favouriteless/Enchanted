package net.favouriteless.enchanted.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.favouriteless.enchanted.fabric.common.CommonEventsFabric;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method = "method_41929", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"))
    private void itemBreakUseItem(InteractionHand interactionHand, Player player, MutableObject mutableObject, int i, CallbackInfoReturnable<Packet> cir,
                                  @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        if (itemStack2.isEmpty()) {
            CommonEventsFabric.playerDestroyItemEvent(player, itemStack, interactionHand);
        }
    }
}
