package com.favouriteless.enchanted.mixin.fabric;

import com.favouriteless.enchanted.common.FabricCommonEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected ItemStack useItem;

    @Shadow public abstract InteractionHand getUsedItemHand();

    @Inject(method="releaseUsingItem", at=@At(value="INVOKE", target="Lnet/minecraft/world/item/ItemStack;releaseUsing(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V", shift=Shift.AFTER))
    private void itemBreakReleaseUsingItem(CallbackInfo ci) {
        if((Object)this instanceof Player && useItem != null && useItem.isEmpty())
            FabricCommonEvents.playerDestroyItemEvent(((Player)(Object)this), useItem, getUsedItemHand());
    }

}
