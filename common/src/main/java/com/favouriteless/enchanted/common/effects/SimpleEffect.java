package com.favouriteless.enchanted.common.effects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import javax.annotation.Nullable;

/**
 * This class should not have to exist in any way or for any reason, I just had to add it to fix mojang's janky ass Effect class with a protected constructor and giant else if chains.
 */
public class SimpleEffect extends MobEffect {

	public SimpleEffect(MobEffectCategory effectType, int color) {
		super(effectType, color);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

	}

	@Override
	public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entity, int amplifier, double health) {

	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return false;
	}

}
