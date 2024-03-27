package com.favouriteless.enchanted.common.items.poppets;

import com.favouriteless.enchanted.common.poppet.PoppetColour;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class VoidPoppetEffectItem extends DeathPoppetEffectItem {

	public VoidPoppetEffectItem(float failRate, int durability, PoppetColour colour, Predicate<DamageSource> sourcePredicate, Supplier<MobEffectInstance> deathEffect) {
		super(failRate, durability, colour, sourcePredicate, deathEffect);
	}

	@Override
	public boolean canProtect(Player player) {
		return true;
	}

	@Override
	public void protect(Player player) {
		player.setHealth(1);
		Vec3 pos = player.position();
		player.teleportTo(pos.x, 257.0D, pos.z);
		player.addEffect(deathEffect.get());
	}

}
