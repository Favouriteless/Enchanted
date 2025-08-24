package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.common.poppet.PoppetColour;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class FirePoppetItem extends DeathPoppetItem {

	public FirePoppetItem(PoppetColour colour, Predicate<DamageSource> sourcePredicate, Properties properties) {
		super(colour, sourcePredicate, properties);
	}

	@Override
	public void protect(Player player) {
		player.setHealth(1);
		player.clearFire();
	}

}
