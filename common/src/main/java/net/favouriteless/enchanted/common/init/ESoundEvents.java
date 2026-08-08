package net.favouriteless.enchanted.common.init;

import net.favouriteless.enchanted.common.Enchanted;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ESoundEvents {

    public static Holder<SoundEvent> BIND_FAMILIAR = register("bind_familiar", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("bind_familiar")));
    public static Holder<SoundEvent> BROOM_SWEEP = register("broom_sweep", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("broom_sweep")));
    public static Holder<SoundEvent> CAULDRON_BUBBLING = register("cauldron_bubbling", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("cauldron_bubbling")));
    public static Holder<SoundEvent> CHALK_WRITE = register("chalk_write", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("chalk_write")));
    public static Holder<SoundEvent> CURSE_CAST = register("curse_cast", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("curse_cast")));
    public static Holder<SoundEvent> CURSE_WHISPER = register("curse_whisper", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("curse_whisper")));
    public static Holder<SoundEvent> MORTAR = register("mortar", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("mortar")));
    public static Holder<SoundEvent> REMOVE_CURSE = register("remove_curse", () -> SoundEvent.createVariableRangeEvent(Enchanted.id("remove_curse")));

    private static <T extends SoundEvent> Holder<SoundEvent> register(String name, Supplier<T> soundSupplier) {
        return EServices.REGISTRY.registerHolder(BuiltInRegistries.SOUND_EVENT, name, soundSupplier);
    }

    public static void load() {
    } // Method which exists purely to load the class.


}
