package net.favouriteless.enchanted.mixin.common;

import net.favouriteless.enchanted.common.CommonConfig;
import net.favouriteless.enchanted.common.loot_extensions.LootExtensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
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
		if(CommonConfig.INSTANCE.disableTotems.get())
			cir.setReturnValue(false);
	}

	@Inject(method="dropFromLootTable", at=@At("TAIL"), locals=LocalCapture.CAPTURE_FAILSOFT)
	protected void dropFromLootTable(DamageSource damageSource, boolean hitByPlayer, CallbackInfo ci,
									 ResourceKey<LootTable> resourcekey, LootTable table, Builder builder,
									 LootParams lootparams) {
		LootExtensions.tryRollEntity((LivingEntity)(Object)this, builder);
	}

}
