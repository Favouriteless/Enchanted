package net.favouriteless.enchanted.common.enchanted.poppet;

import net.favouriteless.enchanted.common.init.EMobEffects;
import net.favouriteless.enchanted.common.init.ETags.Items;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class PoppetPredicates {

    public static final Predicate<DamageSource> EARTH = s -> s.is(DamageTypeTags.IS_FALL) || s.is(DamageTypes.FLY_INTO_WALL);
    public static final Predicate<DamageSource> FIRE = s -> s.is(DamageTypeTags.IS_FIRE);
    public static final Predicate<DamageSource> HUNGER = s -> s.is(DamageTypes.STARVE);
    public static final Predicate<DamageSource> MAGIC = EMobEffects::isMagic;
    public static final Predicate<DamageSource> VOID = s -> s.is(DamageTypes.FELL_OUT_OF_WORLD);
    public static final Predicate<DamageSource> WATER = s -> s.is(DamageTypeTags.IS_DROWNING);

    public static final Predicate<ItemStack> ARMOUR_PROTECTS = s -> s.is(Items.ARMOUR_POPPET_WHITELIST) && !s.is(Items.ARMOUR_POPPET_BLACKLIST);
    public static final Predicate<ItemStack> TOOL_PROTECTS = s -> s.is(Items.TOOL_POPPET_WHITELIST) && !s.is(Items.TOOL_POPPET_BLACKLIST);

}
