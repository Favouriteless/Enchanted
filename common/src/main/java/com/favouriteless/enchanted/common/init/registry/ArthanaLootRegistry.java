package com.favouriteless.enchanted.common.init.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArthanaLootRegistry {

	private final Map<EntityType<?>, ItemStack> loot = new HashMap<>();

	public void register(EntityType<?> type, ItemStack drop) {
		loot.put(type, drop);
	}

	public ItemStack get(EntityType<?> entityType) {
		return loot.containsKey(entityType) ? loot.get(entityType).copy() : null;
	}

	public void reset() {
		loot.clear();
	}

}
