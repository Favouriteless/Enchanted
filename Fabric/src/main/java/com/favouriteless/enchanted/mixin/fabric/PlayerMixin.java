package com.favouriteless.enchanted.mixin.fabric;

import com.favouriteless.enchanted.common.CommonEventsFabric;
import com.llamalad7.mixinextras.sugar.Local;
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
        CommonEventsFabric.playerDestroyItemEvent(player, player.getUseItem(), hand);
    }

    @Inject(method="interactOn", at=@At(value="RETURN", ordinal=1), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakInteractOn(Entity entityToInteractOn, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir,
                                     ItemStack itemStack, ItemStack itemStack2) {
        if(!abilities.instabuild && itemStack.isEmpty())
            CommonEventsFabric.playerDestroyItemEvent((Player)(Object)this, itemStack2, hand);
    }

    @Inject(method="interactOn", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakInteractOn1(Entity entityToInteractOn, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir,
                                     ItemStack itemStack, ItemStack itemStack2) {
        if(!abilities.instabuild && itemStack.isEmpty())
            CommonEventsFabric.playerDestroyItemEvent((Player)(Object)this, itemStack2, hand);
    }

    @Inject(method="attack", at=@At(value="INVOKE", target="Lnet/minecraft/world/entity/player/Player;setItemInHand(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)V"), locals=LocalCapture.CAPTURE_FAILHARD)
    private void itemBreakAttack(Entity target, CallbackInfo ci, @Local ItemStack itemStack) {
        ItemStack original; // TODO Somehow replace this weird jank with getting an actual copy of the item before it gets passed into ItemStack#hurt.
        if(itemStack.isEmpty()) {
            itemStack.setCount(1);
            original = itemStack.copy();
            original.setDamageValue(original.getMaxDamage() - 1);
            itemStack.setCount(0);
        }
        else
            original = itemStack.copy();

        CommonEventsFabric.playerDestroyItemEvent((Player)(Object)this, original, InteractionHand.MAIN_HAND);
    }

}
