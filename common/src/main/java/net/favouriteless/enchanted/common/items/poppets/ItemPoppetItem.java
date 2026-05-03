package net.favouriteless.enchanted.common.items.poppets;

import net.favouriteless.enchanted.api.poppets.ItemPoppetEffect;
import net.favouriteless.enchanted.common.enchanted.poppet.PoppetColour;
import net.minecraft.world.item.ItemStack;

public class ItemPoppetItem extends PoppetItem {

    private final ItemPoppetEffect effect;

    public ItemPoppetItem(PoppetColour colour, ItemPoppetEffect effect, Properties properties) {
        super(colour, properties);
        this.effect = effect;
    }

    public boolean canProtect(ItemStack stack) {
        return effect.canProtect(stack);
    }

    public void protect(ItemStack stack) {
        effect.protect(stack);
    }

}
