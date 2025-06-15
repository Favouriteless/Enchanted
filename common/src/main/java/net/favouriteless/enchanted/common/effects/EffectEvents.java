package net.favouriteless.enchanted.common.effects;

import net.favouriteless.enchanted.common.init.EMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;

public class EffectEvents {

	public static boolean onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
		if(entity.hasEffect(EMobEffects.FALL_RESISTANCE))
			return source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL);
		if(entity.hasEffect(EMobEffects.DROWN_RESISTANCE))
			return source.is(DamageTypeTags.IS_DROWNING);
		if(entity.hasEffect(EMobEffects.MAGIC_RESISTANCE))
			return EMobEffects.isMagic(source);

		return false;
	}

}
