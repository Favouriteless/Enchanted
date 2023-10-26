package com.favouriteless.enchanted.common.effects;

import com.favouriteless.enchanted.common.init.registry.EnchantedEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

public class EffectEvents {

	public static boolean onLivingHurt(LivingEntity entity, DamageSource source, float amount) {

		if(entity.hasEffect(EnchantedEffects.FALL_RESISTANCE.get()))
			return source == DamageSource.FALL || source == DamageSource.FLY_INTO_WALL;
		if(entity.hasEffect(EnchantedEffects.DROWN_RESISTANCE.get()))
			return source == DamageSource.DROWN;

		return false;
	}

}
