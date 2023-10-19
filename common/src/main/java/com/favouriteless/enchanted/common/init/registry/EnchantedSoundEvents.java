package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class EnchantedSoundEvents {
	
	public static Supplier<SoundEvent> CURSE_WHISPER = register("curse_whisper", () -> new SoundEvent(Enchanted.location("curse_whisper")));
	public static Supplier<SoundEvent> CAULDRON_BUBBLING = register("cauldron_bubbling", () -> new SoundEvent(Enchanted.location("cauldron_bubbling")));
	public static Supplier<SoundEvent> CURSE_CAST = register("curse_cast", () -> new SoundEvent(Enchanted.location("curse_cast")));
	public static Supplier<SoundEvent> REMOVE_CURSE = register("remove_curse", () -> new SoundEvent(Enchanted.location("remove_curse")));
	public static Supplier<SoundEvent> BROOM_SWEEP = register("broom_sweep", () -> new SoundEvent(Enchanted.location("broom_sweep")));
	public static Supplier<SoundEvent> CHALK_WRITE = register("chalk_write", () -> new SoundEvent(Enchanted.location("chalk_write")));
	public static Supplier<SoundEvent> BIND_FAMILIAR = register("bind_familiar", () -> new SoundEvent(Enchanted.location("bind_familiar")));

	public static Supplier<SoundEvent> SILENT = register("silent", () -> new SoundEvent(Enchanted.location("silent"))); // Only exists as a dummy sound to avoid nulls



	private static <T extends SoundEvent> Supplier<T> register(String name, Supplier<T> soundSupplier) {
		return Services.COMMON_REGISTRY.register(Registry.SOUND_EVENT, name, soundSupplier);
	}

	public static void load() {} // Method which exists purely to load the class.


}
