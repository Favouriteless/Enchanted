package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.poppet.PoppetColour;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class FirePoppetEffectItem extends DeathPoppetEffectItem {

	public FirePoppetEffectItem(PoppetColour colour, Supplier<MobEffectInstance> deathEffect,
								Predicate<DamageSource> sourcePredicate, Properties properties) {
		super(colour, deathEffect, sourcePredicate, properties);
	}

	@Override
	public void protect(Player player) {
		player.setHealth(1);
		player.clearFire();
		player.addEffect(deathEffect.get());
	}

}
