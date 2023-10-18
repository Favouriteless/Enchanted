package com.favouriteless.enchanted.mixin;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.poppet.PoppetEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

	@Inject(method="checkTotemDeathProtection", at=@At("HEAD"), cancellable=true)
	private void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(EnchantedConfig.DISABLE_TOTEMS.get()) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method="broadcastBreakEvent(Lnet/minecraft/world/entity/EquipmentSlot;)V", at=@At("HEAD"))
	private void broadcastBreakEvent(EquipmentSlot slot, CallbackInfo ci) {
		PoppetEvents.onLivingEntityBreak((LivingEntity)(Object)this, slot);
	}

}
