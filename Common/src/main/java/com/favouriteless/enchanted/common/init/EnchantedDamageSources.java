package com.favouriteless.enchanted.common.init;


import com.favouriteless.enchanted.platform.Services;
import net.minecraft.world.damagesource.DamageSource;

public class EnchantedDamageSources {

    public static final DamageSource SACRIFICE = Services.COMMON_REGISTRY.getDamageSource("sacrifice", true, true, true, true);

}
