package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.blocks.entity.AltarBlockEntity.AltarUpgrade;

import java.util.ArrayList;
import java.util.List;

public class AltarUpgradeRegistry {

	private final List<AltarUpgrade> upgrades = new ArrayList<>();

	public void register(AltarUpgrade upgrade) {
		upgrades.add(upgrade);
	}

	public List<AltarUpgrade> getAll() {
		return upgrades;
	}

	public void reset() {
		upgrades.clear();
	}

}
