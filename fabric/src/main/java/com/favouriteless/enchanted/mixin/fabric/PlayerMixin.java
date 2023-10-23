package com.favouriteless.enchanted.mixin.fabric;

import com.favouriteless.enchanted.common.FabricCommonEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Player.class)
public class PlayerMixin {

    @Shadow @Final private Abilities abilities;

    @Inject(method="method_20266", at=@At("HEAD"), locals=LocalCapture.CAPTURE_FAILHARD)
    private static void itemBreakHurtCurrentlyUsedShieldLambda(InteractionHand hand, Player player, CallbackInfo ci) {
        FabricCommonEvents.playerDestroyItemEvent(player, player.getUseItem(), hand);
    }

    @Inject(method="interactOn", at=@At(value="RETURN", ordinal=1), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakInteractOn(Entity entityToInteractOn, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir,
                                     ItemStack itemStack, ItemStack itemStack2) {
        if(!abilities.instabuild && itemStack.isEmpty())
            FabricCommonEvents.playerDestroyItemEvent((Player)(Object)this, itemStack2, hand);
    }

    @Inject(method="interactOn", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakInteractOn1(Entity entityToInteractOn, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir,
                                     ItemStack itemStack, ItemStack itemStack2) {
        if(!abilities.instabuild && itemStack.isEmpty())
            FabricCommonEvents.playerDestroyItemEvent((Player)(Object)this, itemStack2, hand);
    }

    @Inject(method="attack", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakAttack(Entity target, CallbackInfo ci,
                                 float f, float g, float h, boolean bl, boolean bl2, int i, boolean bl3, boolean bl4,
                                 float j, int k, ItemStack itemStack2) {
        ItemStack original; // TODO Somehow replace this weird jank with getting an actual copy of the item before it gets passed into ItemStack#hurt.
        if(itemStack2.isEmpty()) {
            itemStack2.setCount(1);
            original = itemStack2.copy();
            original.setDamageValue(original.getMaxDamage() - 1);
            itemStack2.setCount(0);
        }
        else
            original = itemStack2.copy();

        FabricCommonEvents.playerDestroyItemEvent((Player)(Object)this, original, InteractionHand.MAIN_HAND);
    }

}
