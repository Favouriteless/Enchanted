package com.favouriteless.enchanted.common.items.poppets;

import com.favouriteless.enchanted.common.poppet.PoppetColour;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;

import java.util.function.Predicate;

public abstract class AbstractDeathPoppetItem extends AbstractPoppetItem {

	protected final Predicate<DamageSource> sourcePredicate;


	public AbstractDeathPoppetItem(float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate) {
		super(failRate, durability, colour);
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
	 * True if extra requirements are met (for example, random chance)
	 * @return
	 */
	public abstract boolean canProtect(Player player);

	/**
	 * Apply protection effects to the given player
	 * @param player
	 */
	public abstract void protect(Player player);

}
