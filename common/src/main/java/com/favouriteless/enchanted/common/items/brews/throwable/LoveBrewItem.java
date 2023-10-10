package com.favouriteless.enchanted.common.items.brews.throwable;

import com.favouriteless.enchanted.common.items.brews.ThrowableBrewItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class LoveBrewItem extends ThrowableBrewItem {

	public static final double RANGE = 4.0D;

	public LoveBrewItem(Properties properties) {
		super(properties);
	}

	@Override
	public void applyEffect(Entity owner, Level level, Vec3 pos) {
		List<Animal> animals = level.getEntitiesOfClass(Animal.class, new AABB(pos.add(-RANGE, -RANGE, -RANGE), pos.add(RANGE, RANGE, RANGE)));
		for(Animal animal : animals) {
			Player source = owner instanceof Player ? (Player)owner : null;
			animal.setInLove(source);
		}
	}

	@Override
	public int getColour() {
		return 0xF78FEB;
	}

}
