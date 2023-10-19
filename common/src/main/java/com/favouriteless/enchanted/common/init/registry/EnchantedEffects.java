package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.common.effects.SimpleEffect;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public class EnchantedEffects {

	public static final Supplier<MobEffect> FALL_RESISTANCE = register("fall_resistance", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x70503A));
	public static final Supplier<MobEffect> DROWN_RESISTANCE = register("drown_resistance", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x2E5299));



	private static <T extends MobEffect> Supplier<T> register(String name, Supplier<T> effectSupplier) {
		return Services.COMMON_REGISTRY.register(Registry.MOB_EFFECT, name, effectSupplier);
	}

}
