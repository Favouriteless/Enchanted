package net.favouriteless.enchanted.common.init;

import com.mojang.serialization.MapCodec;
import net.favouriteless.enchanted.client.particles.types.*;
import net.favouriteless.enchanted.platform.EServices;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;
import java.util.function.Supplier;

public class EParticleTypes {

    public static final Supplier<ParticleType<DelayedPosOptions>> BIND_FAMILIAR = register("bind_familiar", false, DelayedPosOptions::codec, DelayedPosOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> BIND_FAMILIAR_SEED = register("bind_familiar_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<ColouredCircleOptions>> BLIGHT = register("blight", false, ColouredCircleOptions::codec, ColouredCircleOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> BLIGHT_SEED = register("blight_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<ColourOptions>> BOILING = register("boiling", false, ColourOptions::codec, ColourOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> BROILING_SEED = register("broiling_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<ColourOptions>> CAULDRON_BREW = register("cauldron_brew", false, ColourOptions::codec, ColourOptions::streamCodec);
    public static final Supplier<ParticleType<ColourOptions>> CAULDRON_COOK = register("cauldron_cook", false, ColourOptions::codec, ColourOptions::streamCodec);
    public static final Supplier<ParticleType<ColouredCircleOptions>> CIRCLE_MAGIC = register("circle_magic", false, ColouredCircleOptions::codec, ColouredCircleOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> CURSE_SEED = register("curse_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<ColouredCircleOptions>> FERTILITY = register("fertility", false, ColouredCircleOptions::codec, ColouredCircleOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> FERTILITY_SEED = register("fertility_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ESimpleParticleType> IMPRISONMENT_CAGE = register("imprisonment_cage", () -> new ESimpleParticleType(false));
    public static final Supplier<ESimpleParticleType> IMPRISONMENT_CAGE_SEED = register("imprisonment_cage_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<ColourOptions>> KETTLE_COOK = register("kettle_cook", false, ColourOptions::codec, ColourOptions::streamCodec);
    public static final Supplier<ParticleType<TwoColourOptions>> POPPET = register("poppet", false, TwoColourOptions::codec, TwoColourOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> PROTECTION = register("protection", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<DoubleOptions>> PROTECTION_SEED = register("protection_seed", false, DoubleOptions::codec, DoubleOptions::streamCodec);
    public static final Supplier<ParticleType<DelayedPosOptions>> REMOVE_CURSE = register("remove_curse", false, DelayedPosOptions::codec, DelayedPosOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> REMOVE_CURSE_SEED = register("remove_curse_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<DelayedPosOptions>> SKY_WRATH = register("sky_wrath", false, DelayedPosOptions::codec, DelayedPosOptions::streamCodec);
    public static final Supplier<ESimpleParticleType> SKY_WRATH_SEED = register("sky_wrath_seed", () -> new ESimpleParticleType(false));
    public static final Supplier<ParticleType<DoubleOptions>> TRANSPOSITION_IRON_SEED = register("transposition_iron_seed", false, DoubleOptions::codec, DoubleOptions::streamCodec);

    private static <T extends ParticleType<?>> Supplier<T> register(String name, Supplier<T> particleTypeSupplier) {
        return EServices.REGISTRY.register(BuiltInRegistries.PARTICLE_TYPE, name, particleTypeSupplier);
    }

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(String name, boolean override, Function<ParticleType<T>, MapCodec<T>> codec,
                                                                                   Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec) {
        return register(name, () -> new ParticleType<T>(override) {

            public MapCodec<T> codec() {
                return codec.apply(this);
            }

            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec.apply(this);
            }

        });
    }


    public static void load() {} // Method which exists purely to load the class.

}
