package com.favouriteless.enchanted.common.items.poppets;

import com.favouriteless.enchanted.common.poppet.PoppetColour;
import net.minecraft.world.item.ItemStack;

public class ItemProtectionPoppetItem extends AbstractPoppetItem {

	public float damageMultiplier;

	public ItemProtectionPoppetItem(float failRate, int durability, float damageMultiplier, PoppetColour colour) {
		super(failRate, durability, colour);
		this.damageMultiplier = damageMultiplier;
	}

	public void protect(ItemStack item) {
		item.setDamageValue(Math.round(item.getMaxDamage() * damageMultiplier));
	}

}
