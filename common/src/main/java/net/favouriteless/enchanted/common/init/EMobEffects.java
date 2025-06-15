package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.effects.EMobEffect;
import net.favouriteless.enchanted.platform.CommonServices;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public class EMobEffects {

	public static final Holder<MobEffect> DROWN_RESISTANCE = register("drown_resistance", () -> new EMobEffect(MobEffectCategory.BENEFICIAL, 0x2E5299));
	public static final Holder<MobEffect> FALL_RESISTANCE = register("fall_resistance", () -> new EMobEffect(MobEffectCategory.BENEFICIAL, 0x70503A));
	public static final Holder<MobEffect> MAGIC_RESISTANCE = register("magic_resistance", () -> new EMobEffect(MobEffectCategory.BENEFICIAL, 0xAC4AED));
	public static final Holder<MobEffect> GROTESQUE = register("grotesque", () -> new EMobEffect(MobEffectCategory.BENEFICIAL, 0x737335));

	private static <T extends MobEffect> Holder<MobEffect> register(String name, Supplier<T> effectSupplier) {
		return CommonServices.COMMON_REGISTRY.registerHolder(BuiltInRegistries.MOB_EFFECT, name, effectSupplier);
	}

	public static boolean isMagic(DamageSource source) {
		return source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC);
	}

	public static void load() {} // Method which exists purely to load the class.



}
