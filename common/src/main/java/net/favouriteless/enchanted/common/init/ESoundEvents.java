package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.CommonServices;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ESoundEvents {

	public static Supplier<SoundEvent> BIND_FAMILIAR = register("bind_familiar", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("bind_familiar")));
	public static Supplier<SoundEvent> BROOM_SWEEP = register("broom_sweep", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("broom_sweep")));
	public static Supplier<SoundEvent> CAULDRON_BUBBLING = register("cauldron_bubbling", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("cauldron_bubbling")));
	public static Supplier<SoundEvent> CHALK_WRITE = register("chalk_write", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("chalk_write")));
	public static Supplier<SoundEvent> CURSE_CAST = register("curse_cast", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("curse_cast")));
	public static Supplier<SoundEvent> CURSE_WHISPER = register("curse_whisper", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("curse_whisper")));
	public static Supplier<SoundEvent> REMOVE_CURSE = register("remove_curse", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("remove_curse")));
	public static Supplier<SoundEvent> SILENT = register("silent", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("silent"))); // Only exists as a dummy sound to avoid nulls

	private static <T extends SoundEvent> Supplier<T> register(String name, Supplier<T> soundSupplier) {
		return CommonServices.COMMON_REGISTRY.register(BuiltInRegistries.SOUND_EVENT, name, soundSupplier);
	}

	public static void load() {} // Method which exists purely to load the class.


}
