package net.favouriteless.enchanted.api.poppets;

import net.favouriteless.enchanted.common.items.poppets.ItemPoppetItem;
import net.minecraft.world.item.ItemStack;

/**
 * Protects an {@link ItemStack} when it breaks.
 *
 * @see ItemPoppetItem
 */
public interface ItemPoppetEffect {

    boolean canProtect(ItemStack stack);

    void protect(ItemStack stack);

}
