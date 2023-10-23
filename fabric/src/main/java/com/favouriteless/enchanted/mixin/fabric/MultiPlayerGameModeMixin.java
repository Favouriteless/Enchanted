package com.favouriteless.enchanted.mixin.fabric;

import com.favouriteless.enchanted.common.FabricCommonEvents;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Inject(method="method_41929", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakUseItem(InteractionHand interactionHand, Player player, MutableObject mutableObject, int i, CallbackInfoReturnable<Packet> cir,
                         ServerboundUseItemPacket serverboundUseItemPacket, ItemStack itemStack, InteractionResultHolder<ItemStack> interactionResultHolder, ItemStack itemStack2) {
        if(itemStack2.isEmpty())
            FabricCommonEvents.playerDestroyItemEvent(player, itemStack, interactionHand);
    }
}
