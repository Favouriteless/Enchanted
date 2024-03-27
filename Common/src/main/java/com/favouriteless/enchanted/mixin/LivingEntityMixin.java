package com.favouriteless.enchanted.mixin;

import com.favouriteless.enchanted.common.CommonConfig;
import com.favouriteless.enchanted.common.init.LootExtensions;
import com.favouriteless.enchanted.common.poppet.PoppetEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method="checkTotemDeathProtection", at=@At("HEAD"), cancellable=true)
	private void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(CommonConfig.DISABLE_TOTEMS.get())
			cir.setReturnValue(false);
	}

	@Inject(method="broadcastBreakEvent(Lnet/minecraft/world/entity/EquipmentSlot;)V", at=@At("HEAD"))
	public void broadcastBreakEvent(EquipmentSlot slot, CallbackInfo ci) {
		PoppetEvents.onLivingEntityBreak((LivingEntity)(Object)this, slot);
	}

	@Inject(method="dropFromLootTable", at=@At("TAIL"), locals=LocalCapture.CAPTURE_FAILSOFT)
	protected void dropFromLootTable(DamageSource source, boolean b, CallbackInfo ci, ResourceLocation tableLocation, LootTable table, Builder builder) {
		LootExtensions.tryRollEntity((LivingEntity)(Object)this, builder);
	}

}
