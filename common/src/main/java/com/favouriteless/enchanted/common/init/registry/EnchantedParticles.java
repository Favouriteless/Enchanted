package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.client.particles.types.*;
import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.client.particles.types.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.client.particles.types.TwoToneColouredParticleType.TwoToneColouredData;
import com.favouriteless.enchanted.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

public class EnchantedParticles {

    public static final Supplier<ParticleType<SimpleColouredData>> BOILING = register("boiling", () -> new SimpleColouredParticleType(false));
    public static final Supplier<ParticleType<SimpleColouredData>> CAULDRON_BREW = register("cauldron_brew", () -> new SimpleColouredParticleType(false));
    public static final Supplier<ParticleType<SimpleColouredData>> CAULDRON_COOK = register("cauldron_cook", () -> new SimpleColouredParticleType(false));
    public static final Supplier<ParticleType<SimpleColouredData>> KETTLE_COOK = register("kettle_cook", () -> new SimpleColouredParticleType(false));
    public static final Supplier<ParticleType<CircleMagicData>> CIRCLE_MAGIC = register("circle_magic", () -> new CircleMagicParticleType(false));
    public static final Supplier<ParticleType<TwoToneColouredData>> POPPET = register("poppet", () -> new TwoToneColouredParticleType(false));
    public static final Supplier<SimpleParticleType> IMPRISONMENT_CAGE = register("imprisonment_cage", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<DelayedActionData>> SKY_WRATH = register("sky_wrath", () -> new DelayedActionParticleType(false));
    public static final Supplier<ParticleType<DelayedActionData>> REMOVE_CURSE = register("remove_curse", () -> new DelayedActionParticleType(false));
    public static final Supplier<ParticleType<CircleMagicData>> CURSE_BLIGHT = register("curse_blight", () -> new CircleMagicParticleType(false));
    public static final Supplier<ParticleType<CircleMagicData>> FERTILITY = register("fertility", () -> new CircleMagicParticleType(false));
    public static final Supplier<SimpleParticleType> PROTECTION = register("protection", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<DelayedActionData>> BIND_FAMILIAR = register("bind_familiar", () -> new DelayedActionParticleType(false));


    public static final Supplier<SimpleParticleType> IMPRISONMENT_CAGE_SEED = register("imprisonment_cage_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> TRANSPOSITION_IRON_SEED = register("transposition_iron_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> BROILING_SEED = register("broiling_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> SKY_WRATH_SEED = register("sky_wrath_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> CURSE_SEED = register("curse_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> CURSE_BLIGHT_SEED = register("curse_blight_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> REMOVE_CURSE_SEED = register("remove_curse_seed", () -> new SimpleParticleType(false));
    public static final Supplier<SimpleParticleType> FERTILITY_SEED = register("fertility_seed", () -> new SimpleParticleType(false));
    public static final Supplier<ParticleType<DoubleParticleData>> PROTECTION_SEED = register("protection_seed", () -> new DoubleParticleType(false));
    public static final Supplier<SimpleParticleType> BIND_FAMILIAR_SEED = register("bind_familiar_seed", () -> new SimpleParticleType(false));

    private static <T extends ParticleType<?>> Supplier<T> register(String name, Supplier<T> particleTypeSupplier) {
        return Services.COMMON_REGISTRY.register(Registry.PARTICLE_TYPE, name, particleTypeSupplier);
    }

    public static void load() {} // Method which exists purely to load the class.

}
