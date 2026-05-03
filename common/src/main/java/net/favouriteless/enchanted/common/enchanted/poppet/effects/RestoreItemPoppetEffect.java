package net.favouriteless.enchanted.common.enchanted.poppet.effects;

import net.favouriteless.enchanted.api.poppets.ItemPoppetEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class RestoreItemPoppetEffect implements ItemPoppetEffect {

    private final float restoreMultiplier;
    private final Predicate<ItemStack> protectPredicate;

    public RestoreItemPoppetEffect(float restoreMultiplier, Predicate<ItemStack> protectPredicate) {
        this.restoreMultiplier = restoreMultiplier;
        this.protectPredicate = protectPredicate;
    }

    @Override
    public boolean canProtect(ItemStack stack) {
        return protectPredicate.test(stack);
    }

    @Override
    public void protect(ItemStack stack) {
        stack.setDamageValue(Mth.ceil(stack.getMaxDamage() * (1.0F - restoreMultiplier)));
    }

}
