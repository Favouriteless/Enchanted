package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class DeathPoppetItem extends PoppetItem {

	protected final Predicate<DamageSource> sourcePredicate;

	public DeathPoppetItem(PoppetColour colour, Predicate<DamageSource> sourcePredicate, Properties properties) {
		super(colour, properties);
		this.sourcePredicate = sourcePredicate;
	}

	/**
	 * True if this poppet can protect against the specified DamageSource
	 * @param damageSource
	 * @return
	 */
	public boolean protectsAgainst(DamageSource damageSource) {
		return sourcePredicate.test(damageSource);
	}

	/**
	 * Apply protection effects to the given player
	 * @param player
	 */
	public void protect(Player player) {
		player.setHealth(1);
		player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
	}

}
