/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.mixin;

import com.favouriteless.enchanted.EnchantedConfig;
import com.favouriteless.enchanted.common.events.PoppetEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.DamageSource;
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

	@Inject(method="broadcastBreakEvent(Lnet/minecraft/inventory/EquipmentSlotType;)V", at=@At("HEAD"))
	private void broadcastBreakEvent(EquipmentSlotType slot, CallbackInfo ci) {
		PoppetEvents.onLivingEntityBreak((LivingEntity)(Object)this, slot);
	}

}
