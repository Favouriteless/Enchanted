package com.favouriteless.enchanted.common.init.registry;

import com.favouriteless.enchanted.client.particles.*;
import com.favouriteless.enchanted.client.particles.types.*;
import com.favouriteless.enchanted.client.particles.types.CircleMagicParticleType.CircleMagicData;
import com.favouriteless.enchanted.client.particles.types.DelayedActionParticleType.DelayedActionData;
import com.favouriteless.enchanted.client.particles.types.DoubleParticleType.DoubleParticleData;
import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.client.particles.types.TwoToneColouredParticleType.TwoToneColouredData;
import com.favouriteless.enchanted.platform.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.Item;

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
        return RegistryHandler.register(Registry.PARTICLE_TYPE, name, particleTypeSupplier);
    }

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(BOILING.get(), BoilingParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_BREW.get(), CauldronBrewParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CAULDRON_COOK.get(), CauldronCookParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(KETTLE_COOK.get(), KettleCookParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CIRCLE_MAGIC.get(), CircleMagicParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(POPPET.get(), PoppetParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(IMPRISONMENT_CAGE.get(), ImprisonmentCageParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(IMPRISONMENT_CAGE_SEED.get(), ImprisonmentCageSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(TRANSPOSITION_IRON_SEED.get(), TranspositionIronSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(BROILING_SEED.get(), BroilingSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(SKY_WRATH_SEED.get(), SkyWrathSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(SKY_WRATH.get(), SkyWrathParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CURSE_SEED.get(), CurseSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CURSE_BLIGHT_SEED.get(), CurseBlightSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(CURSE_BLIGHT.get(), RepellingParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(REMOVE_CURSE_SEED.get(), RemoveCurseSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(REMOVE_CURSE.get(), RemoveCurseParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(FERTILITY_SEED.get(), FertilitySeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(FERTILITY.get(), RepellingParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(PROTECTION_SEED.get(), ProtectionSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(PROTECTION.get(), ProtectionParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(BIND_FAMILIAR_SEED.get(), BindFamiliarSeedParticle.Factory::new);
        Minecraft.getInstance().particleEngine.register(BIND_FAMILIAR.get(), BindFamiliarParticle.Factory::new);
    }

}
