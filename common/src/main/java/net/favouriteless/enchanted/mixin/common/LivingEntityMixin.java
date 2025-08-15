package net.favouriteless.enchanted.mixin.common;

import net.favouriteless.enchanted.common.ServerConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "checkTotemDeathProtection", at=@At("HEAD"), cancellable=true)
	private void checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if(ServerConfig.INSTANCE.disableTotems.get())
			cir.setReturnValue(false);
	}

}
