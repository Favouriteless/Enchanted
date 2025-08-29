package net.favouriteless.enchanted.fabric.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetEvents;
import net.favouriteless.enchanted.fabric.common.CommonEventsFabric;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
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
            CommonEventsFabric.playerDestroyItemEvent(((Player)(Object)this), useItem, getUsedItemHand());
    }

    @WrapOperation(method = "doHurtEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V"))
    private void doHurtEquipment(ItemStack item, int amount, LivingEntity entity, EquipmentSlot slot, Operation<Void> original) {
        if(!PoppetEvents.onArmourHurt(entity, slot, item, amount))
            original.call(item, amount, entity, slot);
    }

}
